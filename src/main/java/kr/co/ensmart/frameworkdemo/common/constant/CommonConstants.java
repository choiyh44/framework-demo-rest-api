package kr.co.ensmart.frameworkdemo.common.constant;

import java.text.SimpleDateFormat;

/**
 * hmg-ecore 공통 상수
 * @author H1905110
 *
 */
public class CommonConstants {

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String DB_LOCALE_LANGUAGE_ZH = "zh";
	public static final String DB_TIME_ZONE_CCT = "CCT";

	public static final String JAVA_TIME_ZONE_UTC = "UTC";

	public static final String COMMA    = ",";
    public static final String PERIOD   = ".";
    public static final String EMPTY    = "";
    public static final String SPACE    = " ";
    public static final String SLASH    = "/";
    public static final String AMPERSAND    = "&";
    public static final String ASTERISK = "*";
    public static final String PIPE     = "|";
    public static final String ZERO     = "0";
    public static final String GAP = " ";
    public static final String ENTER_1 = "\n";
    public static final String ENTER_2 = "\r\n";
    public static final String TAB = "\t";
    public static final String BR = "<br>";
    public static final String NBSP = "&nbsp;";
    public static final String EMSP = "&emsp;";

	/**
     * Paging 상수
     */
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int ROWS_PER_PAGE_20 = 20;

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDD_WITH_DELIM = "yyyy/MM/dd";
    public static final String YYYYMMDD_WITH_DASH_DELIM = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMISS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMISS_WITH_DELIM = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYYMMDDHHMISS_WITH_DASH_DELIM = "yyyy-MM-dd HH:mm:ss";

    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
			return new SimpleDateFormat(YYYYMMDD_WITH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DASH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
			return new SimpleDateFormat(YYYYMMDD_WITH_DASH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
			return new SimpleDateFormat(YYYYMMDDHHMISS_WITH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
			return new SimpleDateFormat(YYYYMMDD);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
			return new SimpleDateFormat(YYYYMMDDHHMISS);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DASH_DELIM = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat(YYYYMMDDHHMISS_WITH_DASH_DELIM);
        }
    };

    // 이메일 템플릿 변수 표현식
    public static final String EMAIL_TMPT_VAR_PREFIX = "${";
    public static final String EMAIL_TMPT_VAR_SUFFIX = "}";
}
