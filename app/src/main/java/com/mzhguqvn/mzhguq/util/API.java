package com.mzhguqvn.mzhguq.util;

/**
 * Created by scene on 2017/3/14.
 */

public class API {

//    //正式环境
//    public static final String URL_PRE = "http://api.lfys8.com/video/";
            //测试环境
    public static final String URL_PRE = "http://tapi.lfys8.com/video/";
    //登录注册 每天只掉一次
    public static final String LOGIN_REGISTER = "user/";
    //女优首页
    public static final String ACTOR = "actor";
    //女优视频列表:actor/videos/{actor_id} 女优视频
    public static final String ACTOR_LIST = "actor/videos/";
    //分类视频:cate/videos/{cate_id} 分类视频
    public static final String CATE_VIDEOS = "cate/videos/";
    //首页热门推荐
    public static final String HOT_RECOMMEND = "index";
    //直播
    public static final String LIVE = "live";
    //支付成功 参数：imei,money,order_id,video_id
    public static final String PAY = "pay";
    //点击支付按钮pay/click/{imei}
    public static final String PAY_CLICK = "pay/click/";
    //视频详情页相关推荐
    public static final String VIDEO_RELATED = "video/related/";
    //获取支付的token参数：imei，price,title,video_id;type;version
    public static final String TOKEN = "pay/get_token";
    //检查支付状态http://api.18kam.net/video/pay/is_success?order_id=12&imei=860635035819277
    public static final String CHECK_ORDER = "pay/is_success";
    //获取订单信息方式2
    public static final String GET_ORDER_INFO_TYPE_2 = "pay/get_token_wqb";
    //短代支付
    public static final String GET_DUANDAI_INFO = "pay/get_token_msm";
    //短代支付提交成功信息
    public static final String NOTIFY_SMS = "pay/notify_sms";
    //获取视频详情评论资源
    public static final String VIDEO_COMMENT = "video/comment";
    //获取直播详情的评论
    public static final String LIVE_COMMENT = "video/live_comment";
    //获取福利社数据
    public static final String BBS_LIST = "bbs";

    //获取会员首页的数据[1 => '体验', 2 => '黄金', 3 => '钻石', 4 => '黑金', 5 => '排行']
    public static final String VIP_INDEX = "position/";

    //片库首页
    public static final String FILM_INDEX = "cate";
    //每隔30s调用一次
    public static final String UPLOAD_INFP = "video/stay/";
    //进入视频详情页的时候调用
    public static final String VIDEO_CLIECKED = "video/click/";
    //磁力链搜索
    public static final String MAGNET = "magnet/";
    //更新
    public static final String UPDATE = "version/";
    //上传log日子
    public static final String LOG = "log";
    //弹幕
    public static final String DANMU = "video/danmu";
    /*
    统计
    'positions' => [1 => '体验', 2 => '黄金', 3 => '钻石', 4 => '黑金', 5 => '排行榜', 6 => '排行榜内页',
     7 => '视频内容', 8 => '视频播放', 9 => '片库', 10 => '片库内页', 11 => '论坛', 12 => '磁力搜索',
      13 => '磁力搜索结果', 14 => '我的'],
     */
    public static final String UPLOAD_CURRENT_PAGE = "position_click";

    //发表评论
    public static final String SEND_COMMEND = "user_comment";
    //商品详情
    public static final String GOODS_DETAIL="goods/get_goods/1";
}
