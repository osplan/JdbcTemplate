<%@ page language="java"  pageEncoding="utf-8"%>
id:<%=com.zenoc.util.DatabaseUtil.getDatabasePriykey()%><br/>
x-forwarded-for:<%=request.getHeader("x-forwarded-for")%><br/>
X-Real_IP:<%=request.getHeader("X-Real-IP")%><br/>
Proxy-Client-IP:<%=request.getHeader("Proxy-Client-IP")%><br/>
WL-Proxy-Client-IP:<%=request.getHeader("WL-Proxy-Client-IP")%><br/>
REMOTE-HOST:<%=request.getHeader("REMOTE-HOST")%><br/>
RemoteAddr:<%=request.getRemoteAddr()%><br/>