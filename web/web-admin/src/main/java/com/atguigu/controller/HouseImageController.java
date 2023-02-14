package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-08  10:08
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    private static final String UPLOAD_SHOW = "house/upload";
    private static final String PAGE_SHOW = "redirect:/house/";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;
    @GetMapping("/uploadShow/{houseId}/{imageType}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("imageType") Integer imageType,
                             Model model){
        //1. 将houseId和imageType保存到请求域
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",imageType);
        return UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{imageType}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("imageType") Integer imageType,
                         @RequestParam("file")MultipartFile multipartFile) throws IOException {
        //houseId就是要上传图片的那个房源的id
        //imageType就是上传的图片的类型，1表示房源图片，2表示房产图片
        //multipartFile表示上传的那张图片
        //1. 将客户端上传的图片保存到七牛云
        //1.1 获取一个唯一的name
        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
        //1.2 构建保存路径
        String filePath = "shf/" + FileUtil.getRandomDirPath(SHFConstant.FileConstant.DEFAULT_DIR_LEVEL,SHFConstant.FileConstant.DEFAULT_DIR_SIZE) + uuidName;
        //1.3 调用工具类的方法上传
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),filePath);
        //2. 将图片的访问路径、在七牛云中的保存路径存储到MySQL的hse_house_image表中
        //2.1 获取上传的图片的访问url
        String url = QiniuUtils.getUrl(filePath);
        //2.2 构建HouseImage对象
        HouseImage houseImage = new HouseImage();
        houseImage.setHouseId(houseId);
        houseImage.setImageUrl(url);
        houseImage.setType(imageType);
        houseImage.setImageName(filePath);
        //2.3 调用业务层的方法保存houseImage
        houseImageService.insert(houseImage);
        //3.判断当前房源是否有默认图片，如果没有则将当前这张图片作为房源的默认图片
        //3.1 根据houseId查询房源信息
        House house = houseService.getById(houseId);
        //创建用于匹配defaultImageUrl的正则
        //3.2 判断是否有默认图片
        if (StringUtils.isEmpty(house.getDefaultImageUrl()) || !Pattern.matches(SHFConstant.FileConstant.IMAGE_URL_REGEXP,house.getDefaultImageUrl())) {
            //说明没有默认图片,将当前图片作为默认图片
            house.setDefaultImageUrl(url);
            //修改house的信息
            houseService.update(house);
        }

        //上传成功
        return Result.ok();
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id){
        //1. 根据图片id查询图片信息
        HouseImage houseImage = houseImageService.getById(id);
        //1. 从七牛云中删除
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //2. 从houseImage表中删除
        houseImageService.delete(id);
        //3. 判断当前要删除的图片是否是房源的默认图片，如果是就要修改房源的默认图片为null
        //3.1 根据房源id查询房源信息
        House house = houseService.getById(houseId);
        //3.2 判断当前这张图片的url和房源的defaultImageUrl是否相同
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //表示当前要删除的图片刚好是房源的默认图片,那么就要将房源的默认图片修改为Null
            house.setDefaultImageUrl("");
            houseService.update(house);
        }

        //显示房源详情页
        return PAGE_SHOW + houseId;
    }
}
