/**
 * 
 */
package com.amap.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteRailwayItem;
import com.share_will.mobile.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AMapUtil {

	static final String LINE_CHAR="=";
	static final String BOARD_CHAR="|";
	static final int LENGTH = 80;
	static String LINE;
	static{
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<LENGTH;i++){
			sb .append(LINE_CHAR);
		}
		LINE = sb.toString();
	}

	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}

	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) // 10 km
		{
			int dis = lenMeter / 1000;
			return dis + ChString.Kilometer;
		}

		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + ChString.Kilometer;
		}

		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + ChString.Meter;
		}

		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}

		return dis + ChString.Meter;
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	/**
	 * 把LatLonPoint对象转化为LatLon对象
	 */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	/**
	 * 把集合体的LatLonPoint转化为集合体的LatLng
	 */
	public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = AMapUtil.convertToLatLng(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
	
	public static String getFriendlyTime(int second) {
		if (second > 3600) {
			int hour = second / 3600;
			int miniate = (second % 3600) / 60;
			return hour + "小时" + miniate + "分钟";
		}
		if (second >= 60) {
			int miniate = second / 60;
			return miniate + "分钟";
		}
		return second + "秒";
	}
	
	//路径规划方向指示和图片对应
		public static int getDriveActionID(String actionName) {
			if (actionName == null || actionName.equals("")) {
				return R.drawable.dir3;
			}
			if ("左转".equals(actionName)) {
				return R.drawable.dir2;
			}
			if ("右转".equals(actionName)) {
				return R.drawable.dir1;
			}
			if ("向左前方行驶".equals(actionName) || "靠左".equals(actionName)) {
				return R.drawable.dir6;
			}
			if ("向右前方行驶".equals(actionName) || "靠右".equals(actionName)) {
				return R.drawable.dir5;
			}
			if ("向左后方行驶".equals(actionName) || "左转调头".equals(actionName)) {
				return R.drawable.dir7;
			}
			if ("向右后方行驶".equals(actionName)) {
				return R.drawable.dir8;
			}
			if ("直行".equals(actionName)) {
				return R.drawable.dir3;
			}
			if ("减速行驶".equals(actionName)) {
				return R.drawable.dir4;
			}
			return R.drawable.dir3;
		}
		
		public static int getWalkActionID(String actionName) {
			if (actionName == null || actionName.equals("")) {
				return R.drawable.dir13;
			}
			if ("左转".equals(actionName)) {
				return R.drawable.dir2;
			}
			if ("右转".equals(actionName)) {
				return R.drawable.dir1;
			}
			if ("向左前方".equals(actionName) || "靠左".equals(actionName) || actionName.contains("向左前方")) {
				return R.drawable.dir6;
			}
			if ("向右前方".equals(actionName) || "靠右".equals(actionName) || actionName.contains("向右前方")) {
				return R.drawable.dir5;
			}
			if ("向左后方".equals(actionName)|| actionName.contains("向左后方")) {
				return R.drawable.dir7;
			}
			if ("向右后方".equals(actionName)|| actionName.contains("向右后方")) {
				return R.drawable.dir8;
			}
			if ("直行".equals(actionName)) {
				return R.drawable.dir3;
			}
			if ("通过人行横道".equals(actionName)) {
				return R.drawable.dir9;
			}
			if ("通过过街天桥".equals(actionName)) {
				return R.drawable.dir11;
			}
			if ("通过地下通道".equals(actionName)) {
				return R.drawable.dir10;
			}

			return R.drawable.dir13;
		}
		
		public static String getBusPathTitle(BusPath busPath) {
			if (busPath == null) {
				return String.valueOf("");
			}
			List<BusStep> busSetps = busPath.getSteps();
			if (busSetps == null) {
				return String.valueOf("");
			}
			StringBuffer sb = new StringBuffer();
			for (BusStep busStep : busSetps) {
				 StringBuffer title = new StringBuffer();
			   if (busStep.getBusLines().size() > 0) {
				   for (RouteBusLineItem busline : busStep.getBusLines()) {
					   if (busline == null) {
							continue;
						}
					  
					   String buslineName = getSimpleBusLineName(busline.getBusLineName());
					   title.append(buslineName);
					   title.append(" / ");
				}
//					RouteBusLineItem busline = busStep.getBusLines().get(0);
				   
					sb.append(title.substring(0, title.length() - 3));
					sb.append(" > ");
				}
				if (busStep.getRailway() != null) {
					RouteRailwayItem railway = busStep.getRailway();
					sb.append(railway.getTrip()+"("+railway.getDeparturestop().getName()
							+" - "+railway.getArrivalstop().getName()+")");
					sb.append(" > ");
				}
			}
			return sb.substring(0, sb.length() - 3);
		}

		public static String getBusPathDes(BusPath busPath) {
			if (busPath == null) {
				return String.valueOf("");
			}
			long second = busPath.getDuration();
			String time = getFriendlyTime((int) second);
			float subDistance = busPath.getDistance();
			String subDis = getFriendlyLength((int) subDistance);
			float walkDistance = busPath.getWalkDistance();
			String walkDis = getFriendlyLength((int) walkDistance);
			return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
		}
		
		public static String getSimpleBusLineName(String busLineName) {
			if (busLineName == null) {
				return String.valueOf("");
			}
			return busLineName.replaceAll("\\(.*?\\)", "");
		}

	public static void showerror(Context context, int rCode){

		try {
			switch (rCode) {
				//服务错误码
				case 1001:
					throw new AMapException(AMapException.AMAP_SIGNATURE_ERROR);
				case 1002:
					throw new AMapException(AMapException.AMAP_INVALID_USER_KEY);
				case 1003:
					throw new AMapException(AMapException.AMAP_SERVICE_NOT_AVAILBALE);
				case 1004:
					throw new AMapException(AMapException.AMAP_DAILY_QUERY_OVER_LIMIT);
				case 1005:
					throw new AMapException(AMapException.AMAP_ACCESS_TOO_FREQUENT);
				case 1006:
					throw new AMapException(AMapException.AMAP_INVALID_USER_IP);
				case 1007:
					throw new AMapException(AMapException.AMAP_INVALID_USER_DOMAIN);
				case 1008:
					throw new AMapException(AMapException.AMAP_INVALID_USER_SCODE);
				case 1009:
					throw new AMapException(AMapException.AMAP_USERKEY_PLAT_NOMATCH);
				case 1010:
					throw new AMapException(AMapException.AMAP_IP_QUERY_OVER_LIMIT);
				case 1011:
					throw new AMapException(AMapException.AMAP_NOT_SUPPORT_HTTPS);
				case 1012:
					throw new AMapException(AMapException.AMAP_INSUFFICIENT_PRIVILEGES);
				case 1013:
					throw new AMapException(AMapException.AMAP_USER_KEY_RECYCLED);
				case 1100:
					throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_ERROR);
				case 1101:
					throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR);
				case 1102:
					throw new AMapException(AMapException.AMAP_ENGINE_CONNECT_TIMEOUT);
				case 1103:
					throw new AMapException(AMapException.AMAP_ENGINE_RETURN_TIMEOUT);
				case 1200:
					throw new AMapException(AMapException.AMAP_SERVICE_INVALID_PARAMS);
				case 1201:
					throw new AMapException(AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS);
				case 1202:
					throw new AMapException(AMapException.AMAP_SERVICE_ILLEGAL_REQUEST);
				case 1203:
					throw new AMapException(AMapException.AMAP_SERVICE_UNKNOWN_ERROR);
					//sdk返回错误
				case 1800:
					throw new AMapException(AMapException.AMAP_CLIENT_ERRORCODE_MISSSING);
				case 1801:
					throw new AMapException(AMapException.AMAP_CLIENT_ERROR_PROTOCOL);
				case 1802:
					throw new AMapException(AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION);
				case 1803:
					throw new AMapException(AMapException.AMAP_CLIENT_URL_EXCEPTION);
				case 1804:
					throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION);
				case 1806:
					throw new AMapException(AMapException.AMAP_CLIENT_NETWORK_EXCEPTION);
				case 1900:
					throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWN_ERROR);
				case 1901:
					throw new AMapException(AMapException.AMAP_CLIENT_INVALID_PARAMETER);
				case 1902:
					throw new AMapException(AMapException.AMAP_CLIENT_IO_EXCEPTION);
				case 1903:
					throw new AMapException(AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION);
					//云图和附近错误码
				case 2000:
					throw new AMapException(AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST);
				case 2001:
					throw new AMapException(AMapException.AMAP_ID_NOT_EXIST);
				case 2002:
					throw new AMapException(AMapException.AMAP_SERVICE_MAINTENANCE);
				case 2003:
					throw new AMapException(AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST);
				case 2100:
					throw new AMapException(AMapException.AMAP_NEARBY_INVALID_USERID);
				case 2101:
					throw new AMapException(AMapException.AMAP_NEARBY_KEY_NOT_BIND);
				case 2200:
					throw new AMapException(AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR);
				case 2201:
					throw new AMapException(AMapException.AMAP_CLIENT_USERID_ILLEGAL);
				case 2202:
					throw new AMapException(AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT);
				case 2203:
					throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT);
				case 2204:
					throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR);
					//路径规划
				case 3000:
					throw new AMapException(AMapException.AMAP_ROUTE_OUT_OF_SERVICE);
				case 3001:
					throw new AMapException(AMapException.AMAP_ROUTE_NO_ROADS_NEARBY);
				case 3002:
					throw new AMapException(AMapException.AMAP_ROUTE_FAIL);
				case 3003:
					throw new AMapException(AMapException.AMAP_OVER_DIRECTION_RANGE);
					//短传分享
				case 4000:
					throw new AMapException(AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED);
				case 4001:
					throw new AMapException(AMapException.AMAP_SHARE_FAILURE);
				default:
					Toast.makeText(context,"查询失败："+rCode , Toast.LENGTH_LONG).show();
					logError("查询失败", rCode);
					break;
			}
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			logError(e.getMessage(), rCode);
		}
	}

	private static void logError(String info, int errorCode) {
		print(LINE);//start
		print("                                   错误信息                                     ");
		print(LINE);//title
		print(info);
		print("错误码: " + errorCode);
		print("                                                                               ");
		print("如果需要更多信息，请根据错误码到以下地址进行查询");
		print("  http://lbs.amap.com/api/android-sdk/guide/map-tools/error-code/");
		print("如若仍无法解决问题，请将全部log信息提交到工单系统，多谢合作");
		print(LINE);//end
	}

	private static void print(String s) {
		Log.d("map", s);
	}
}
