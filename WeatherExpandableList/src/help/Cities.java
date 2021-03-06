package help;

import java.util.HashMap;
import java.util.TreeSet;

public class Cities {
	public static final String[] CITIES = new String[] { "Tokyo, Japan",
			"Jakarta, Indonesia", "New York, United States",
			"Seoul, South Korea", "Manila, Philippines", "Mumbai, India",
			"Sao Paulo, Brazil", "Mexico City, Mexico", "Delhi, India",
			"Osaka, Japan", "Cairo, Egypt", "Kolkata, India",
			"Los Angeles, United States", "Shanghai, China", "Moscow, Russia",
			"Beijing, China", "Buenos Aires, Argentina", "Guangzhou, China",
			"Shenzhen, China", "Istanbul, Turkey", "Rio De Janeiro, Brazil",
			"Paris, France", "Karachi, Pakistan", "Nagoya, Japan",
			"Chicago, United States", "Lagos, Nigeria",
			"London, United Kingdom", "Bangkok, Thailand",
			"Kinshasa, Dem Rep Of Congo", "Tehran, Iran", "Lima, Peru",
			"Dongguan, China", "Bogota, Colombia", "Chennai, India",
			"Dhaka, Bangladesh", "Essen, Germany", "Tianjin, China",
			"Hong Kong, China", "Taipei, Taiwan Roc", "Lahore, Pakistan",
			"Ho Chi Minh City, Viet Nam", "Bangalore, India",
			"Hyderabad, India", "Johannesburg, South Africa", "Baghdad, Iraq",
			"Toronto, Canada", "Santiago, Chile", "Kuala Lumpur, Malaysia",
			"San Francisco, United States", "Philadelphia, United States",
			"Wuhan, China", "Miami, United States", "Dallas, United States",
			"Madrid, Spain", "Ahmedabad, India", "Boston, United States",
			"Belo Horizonte, Brazil", "Khartoum, Sudan",
			"Saint Petersburg, Russia", "Shenyang, China",
			"Houston, United States", "Pune, India", "Riyadh, Saudi Arabia",
			"Singapore, Singapore", "Washington, United States",
			"Yangon, Myanmar", "Milan, Italy", "Atlanta, United States",
			"Chongqing, China", "Alexandria, Egypt", "Nanjing, China",
			"Guadalajara, Mexico", "Barcelona, Spain", "Chengdu, China",
			"Detroit, United States", "Ankara, Turkey", "Athens, Greece",
			"Berlin, Germany", "Sydney, Australia", "Monterrey, Mexico",
			"Phoenix, United States", "Busan, South Korea", "Recife, Brazil",
			"Bandung, Indonesia", "Porto Alegre, Brazil",
			"Melbourne, Australia", "Luanda, Angola", "Hangzhou, China",
			"Algiers, Algeria", "Pyongyang, North Korea", "Qingdao, China",
			"Surat, India", "Fortaleza, Brazil", "Durban, South Africa",
			"Kanpur, India", "Addis Ababa, Ethiopia", "Nairobi, Kenya",
			"Jeddah, Saudi Arabia", "Naples, Italy", "Kabul, Afghanistan",
			"Salvador, Brazil", "Harbin, China", "Kano, Nigeria",
			"Casablanca, Morocco", "Cape Town, South Africa",
			"Curitiba, Brazil", "Surabaya, Indonesia",
			"San Diego, United States", "Seattle, United States",
			"Rome, Italy", "Dar Es Salaam, Tanzania", "Taichung, China",
			"Jaipur, India", "Caracas, Venezuela", "Dakar, Senegal",
			"Kaohsiung, China", "Minneapolis, United States", "Lucknow, India",
			"Amman, Jordan", "Guayaquil, Ecuador", "Kyiv, Ukraine",
			"Faisalabad, Pakistan", "Mashhad, Iran", "Izmir, Turkey",
			"Rawalpindi, Pakistan", "Tashkent, Uzbekistan", "Katowice, Poland",
			"Changchun, China", "Campinas, Brazil", "Daegu, South Korea",
			"Changsha, China", "Nagpur, India", "San Juan, Philippines",
			"Aleppo, Syria", "Lisbon, Portugal", "Frankfurt Am Main, Germany",
			"Nanchang, China", "Birmingham, United Kingdom",
			"Tampa, United States", "Medan, Indonesia", "Dalian, China",
			"Tunis, Tunisia", "Shijiazhuang, China",
			"Manchester, United Kingdom", "Damascus, Syria", "Fukuoka, Japan",
			"Santo Domingo, Dominican Republic", "Havana, Cuba",
			"Cali, Colombia", "Denver, United States", "Colombo, Brazil",
			"Dubai, United Arab Emirates", "Baltimore, United States",
			"Sapporo, Japan", "Rotterdam, Netherlands", "Vancouver, Canada",
			"Preston, United Kingdom", "Patna, India", "Warsaw, Poland",
			"Bonn, Germany", "Accra, Ghana", "Bucharest, Romania",
			"Yokohama, Japan", "Kunming, China", "Guiyang, China",
			"Zibo, China", "Incheon, South Korea", "Zhengzhou, China",
			"Taiyuan, China", "Chaoyang, China", "Brasilia, Brazil",
			"Zhongshan, China", "West Midlands, United Kingdom", "Giza, Egypt",
			"Quezon City, Philippines", "Nanhai, China", "Fuzhou, China",
			"Lanzhou, China", "Xiamen, China", "Chittagong, Bangladesh",
			"Zaozhuang, China", "Jilin, China", "Linyi, China",
			"Wenzhou, China", "Stockholm, Sweden",
			"Puebla De Zaragoza, Mexico", "Puning, China", "Baku, Azerbaijan",
			"Ibadan, Nigeria", "Brisbane, Australia", "Minsk, Belarus",
			"Sikasso, Mali", "Nanchong, China", "Nanning, China",
			"Urumqi, China", "Yantai, China", "Fuyang, China",
			"Tangshan, China", "Maracaibo, Venezuela", "Hamburg, Germany",
			"Budapest, Hungary", "Shunde, China", "Manaus, Brazil",
			"Xuzhou, China", "Baotou, China", "Hefei, China",
			"Vienna, Austria", "Indore, India", "Asuncion, Paraguay",
			"Tianmen, China", "Belgrade, Serbia", "Suzhou, China",
			"Suizhou, China", "Nanyang, China", "Nakuru, Kenya",
			"Koulikoro, Mali", "Ningbo, China", "Liuan, China",
			"Anshan, China", "Tengzhou, China", "Qiqihaer, China",
			"Pizhou, China", "Taian, China", "Datong, China", "Kobe, Japan",
			"Hama, Syria", "Esfahan, Iran", "Tripoli, Libya",
			"West Yorkshire, United Kingdom", "Vadodara, India",
			"Taizhou, China", "Luoyang, China", "Quito, Ecuador",
			"Jinjiang, China", "Mopti, Mali", "Perth, Australia",
			"Daejeon, South Korea", "Kyoto, Japan", "Xiantao, China",
			"Tangerang, Indonesia", "Bhopal, India", "Coimbatore, India",
			"Kharkiv, Ukraine", "Gwangju, South Korea", "Xinghua, China",
			"Harare, Zimbabwe", "Fushun, China", "Shangqiu, China",
			"Wuxi, China", "Hechuan, China", "Wujin, China", "Guigang, China",
			"Jianyang, China", "Huhehaote, China", "Santa Cruz, Bolivia",
			"Semarang, Indonesia", "Ludhiana, India", "Novosibirsk, Russia",
			"Neijiang, China", "Maputo, Mozambique", "Douala, Cameroon",
			"Weifang, China", "Daqing, China", "Kayes, Mali",
			"Tongzhou, China", "Tabriz, Iran", "Homs, Syria", "Rugao, China",
			"Guiping, China", "Huainan, China", "Kochi, India",
			"Suining, China", "Bozhou, China", "Zhanjiang, China",
			"Changde, China", "Montevideo, Uruguay", "Suzhou, China",
			"Xintai, China", "Yekaterinburg, Russia", "Handan, China",
			"Visakhapatnam, India", "Kawasaki, Japan", "Jiangjin, China",
			"Pingdu, China", "Agra, India", "Jiangyin, China",
			"Tijuana, Mexico", "Liuyang, China", "Bursa, Turkey",
			"Makkah, Saudi Arabia", "Yaounde, Cameroon", "Xuanwei, China",
			"Dengzhou, China", "Palembang, Indonesia",
			"Nizhniy Novgorod, Russia", "Guarulhos, Brazil", "Heze, China",
			"Auckland, New Zealand", "Omdurman, Sudan", "Shantou, China",
			"Leizhou, China", "Yongcheng, China", "Valencia, Venezuela",
			"Thane, India", "San Antonio, United States", "Xinyang, China",
			"Luzhou, China", "Almaty, Kazakhstan", "Changshu, China",
			"Taixing, China", "Phnom Penh, Cambodia", "Laiwu, China",
			"Xiaoshan, China", "Yiyang, China", "Liuzhou, China",
			"Gaozhou, China", "Fengcheng, China", "Cixi, China", "Karaj, Iran",
			"Mogadishu, Somalia", "Varanasi, India", "Kampala, Uganda",
			"Ruian, China", "Lianjiang, China", "Huaian, China",
			"Shiraz, Iran", "Multan, Pakistan", "Madurai, India",
			"Kalyan, India", "Quanzhou, China", "Adana, Turkey",
			"Bazhong, China", "Ouagadougou, Burkina Faso", "Haicheng, China",
			"Xishan, China", "Leiyang, China", "Caloocan, Philippines",
			"Kalookan, Philippines", "Jingzhou, China", "Saitama, Japan",
			"Prague, Czech Republic", "Fuqing, China", "Kumasi, Ghana",
			"Meerut, India", "Hyderabad, Pakistan", "Lufeng, China",
			"Dongtai, China", "Yixing, China", "Mianyang, China",
			"Wenling, China", "Leqing, China", "Ottawa, Canada",
			"Yushu, China", "Barranquilla, Colombia", "Hiroshima, Japan",
			"Chifeng, China", "Nashik, India", "Sofia, Bulgaria",
			"Rizhao, China", "Davao, Philippines", "Tianshui, China",
			"Huzhou, China", "Omsk, Russia", "Gujranwala, Pakistan",
			"Adelaide, Australia", "Macheng, China", "Wuxian, China",
			"Bijie, China", "Yuzhou, China", "Leshan, China",
			"La Matanza, Argentina", "Rosario, Argentina", "Jabalpur, India",
			"Kazan, Russia", "Jimo, China", "Dingzhou, China",
			"Calgary, Canada", "Yerevan, Armenia", "Jamshedpur, India",
			"Zoucheng, China", "Anqiu, China", "Chelyabinsk, Russia",
			"Conakry, Guinea", "Asansol, India", "Shouguang, China",
			"Changzhou, China", "Ulsan, South Korea", "Zhuji, China",
			"Marrakech, Morocco", "Dhanbad, India", "Tbilisi, Georgia",
			"Hanchuan, China", "Lusaka, Zambia", "Qidong, China",
			"Faridabad, India", "Zaoyang, China", "Zhucheng, China",
			"Jiangdu, China", "Xiangcheng, China", "Zigong, China",
			"Jining, China", "Edmonton, Canada", "Allahabad, India",
			"Beiliu, China", "Dnipropetrovsk, Ukraine", "Gongzhuling, China",
			"Qinzhou, China", "Sendai, Japan", "Volgograd, Russia",
			"Ezhou, China", "Guatemala City, Guatemala", "Zhongxiang, China",
			"Amsterdam, Netherlands", "Brussels, Belgium", "Bamako, Mali",
			"Ziyang, China", "Antananarivo, Madagascar", "Mudanjiang, China",
			"Amritsar, India", "Vijayawada, India", "Haora, India",
			"Huazhou, China", "Fuzhou, China", "Pimpri Chinchwad, India",
			"Dublin, Ireland", "Rajkot, India", "Lianyuan, China",
			"Liupanshui, China", "Kaduna, Nigeria", "Kitakyushu, Japan",
			"Qianjiang, China", "Odessa, Ukraine", "Qom, Iran",
			"Yongchuan, China", "Peshawar, Pakistan", "Linzhou, China",
			"Benxi, China", "Ulaanbaatar, Mongolia", "Zhangqiu, China",
			"Yongzhou, China", "Srinagar, India", "Ghaziabad, India",
			"Xinyi, China", "Zhangjiagang, China", "Wafangdian, China",
			"Xianyang, China", "Liaocheng, China", "Ahwaz, Iran",
			"Taishan, China", "Linhai, China", "Feicheng, China",
			"Suwon, South Korea", "Wuwei, China", "Haimen, China",
			"Liling, China", "Xinhui, China", "Gaziantep, Turkey",
			"Krasnoyarsk, Russia", "Chiba, Japan", "Voronezh, Russia",
			"Ruzhou, China", "Yichun, China" };
	public static HashMap<String, String> states = new HashMap<String, String>();
	public static HashMap<String, String> getCountryByCity = new HashMap<String, String>();
	public static TreeSet<String> set = new TreeSet<String>();
	static {
		for (int i = 0; i < CITIES.length; i++) {
			set.add(CITIES[i]);
		}
		states.put("New_York", "New_York");
		states.put("Los_Angeles", "California");
		states.put("Chicago", "Illinois");
		states.put("San_Francisco", "California");
		states.put("Philadelphia", "Pennsylvania");
		states.put("Miami", "Florida");
		states.put("Dallas", "Texas");
		states.put("Boston", "Massachusetts");
		states.put("Houston", "Texas");
		states.put("Atlanta", "Georgia");
		states.put("Detroit", "Michigan");
		states.put("Phoenix", "Arizona");
		states.put("San_Diego", "California");
		states.put("Seattle", "Washington");
		states.put("Minneapolis", "Minnesota");
		states.put("Tampa", "Florida");
		states.put("Denver", "Colorado");
		states.put("Baltimore", "Maryland");
		states.put("San_Antonio", "Texas");
		for (int i = 0; i < CITIES.length; i++) {
			getCountryByCity.put(getCity(CITIES[i]), getCountry(CITIES[i]));
		}
	}

	public static String getCity(int index) {
		String ans = "";
		for (int i = 0; i < CITIES[index].length(); i++) {
			if (CITIES[index].charAt(i) == ',') {
				return ans;
			}
			ans += CITIES[index].charAt(i);
		}
		return ans;
	}

	public static String getCity(String data) {
		String ans = "";
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == ',') {
				return ans;
			}
			if (data.charAt(i) == ' ') {
				ans += '_';
			} else {
				ans += data.charAt(i);
			}
		}
		return ans;
	}

	public static String getOKCity(String data) {
		String ans = "";
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == ',') {
				return ans;
			}
			ans += data.charAt(i);
		}
		return ans;
	}

	public static String getCountry(String data) {
		String ans = "";
		int num = 0;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == ',') {
				num = i + 2;
				break;
			}
		}
		for (int i = num; i < data.length(); i++) {
			if (data.charAt(i) == ' ') {
				ans += '_';
			} else {
				ans += data.charAt(i);
			}
		}
		return ans;
	}
}
