# huobiapi封装
	Restful方式只对获取账户和获取账户余额这两个需要签名的接口进行了封装
	借鉴了github其他封装项目和https://github.com/huobiapi/API_Docs/wiki/REST_authentication
	
## RestFul增加了代理访问
	private HttpUtilManager() {
		// ip, port
		HttpHost proxy = new HttpHost("xxx.xxx.xxx.xxx", xxxx, "http");
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		client = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(keepAliveStrat).setRoutePlanner(routePlanner).build();
	}	
	
## RestFul增加了将现金账户所有货币转换为ETH
