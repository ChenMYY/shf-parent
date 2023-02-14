package com.atguigu.constants;

/**
 * 包名:com.atguigu.constants
 *
 * @author Leevi
 * 日期2023-02-07  10:45
 */
public class SHFConstant {
    /**
     * Redis中的常量
     */
    public static class RedisConstant{
        public static final Integer CODE_REGISTER_EX = 300;
    }
    /**
     * 分页的常量
     */
    public static class PaginationConstant{
        /**
         * 默认的pageNum
         */
        public static final Integer DEFAULT_PAGE_NUM = 1;
        /**
         * 默认的pageSize
         */
        public static final Integer DEFAULT_PAGE_SIZE = 5;
        /**
         * 默认的页码数
         */
        public static final Integer DEFAULT_NAVIGATION_PAGES = 10;
    }

    /**
     * 文件常量
     */
    public static class FileConstant{
        /**
         * 默认的目录层级
         */
        public static final Integer DEFAULT_DIR_LEVEL = 2;
        /**
         * 默认的每级目录的字符数
         */
        public static final Integer DEFAULT_DIR_SIZE = 2;

        public static final String IMAGE_URL_REGEXP = "^(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$";
    }
    /**
     * 字典常量
     */
    public static class DictConstant{
        public static final String HOUSE_TYPE = "houseType";
        public static final String FLOOR = "floor";
        public static final String BUILD_STRUCTURE = "buildStructure";
        public static final String DIRECTION = "direction";
        public static final String DECORATION = "decoration";
        public static final String HOUSE_USE = "houseUse";
        public static final String ROOT = "ROOT";
        public static final String PROVINCE = "province";
        public static final String BEIJING = "beijing";
    }
}
