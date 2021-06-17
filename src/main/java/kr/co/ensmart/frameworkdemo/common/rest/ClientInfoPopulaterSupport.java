package kr.co.ensmart.frameworkdemo.common.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.springframework.util.CollectionUtils;

import kr.co.ensmart.frameworkdemo.common.dto.base.BaseCommonEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientInfoPopulaterSupport {

	public void populateClientInfo(Object o) {
		if ( Objects.isNull(o) ) {
			return;
		}
		
		if ( log.isDebugEnabled() ) {
			log.debug("[CLIENT_INFO_POPULATER]: arg-class={}", o.getClass());
		}
		if ( o instanceof Collection ) {
			populateClientInfo((Collection<?>)o);
		} else if ( o instanceof Map ) {
			populateClientInfo((Map<?,?>)o);
		} else if ( o.getClass().isArray() ) {
			populateClientInfo(Arrays.asList((Object[])o));
		} else if ( o instanceof BaseCommonEntity ) {
			ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
			
			if ( log.isDebugEnabled() ) {
				log.debug("[CLIENT_INFO_POPULATER]: client-info={}", clientInfo);
			}

			if ( Objects.nonNull(clientInfo) ) {
				BaseCommonEntity entity = (BaseCommonEntity)o;
				entity.setDbLocaleLanguage(clientInfo.getDbLocaleLanguage());
				entity.setDbTimeZone(clientInfo.getDbTimeZone());
				entity.setJavaTimeZone(TimeZone.getTimeZone(clientInfo.getJavaTimeZone()));
			}
		}
	}
	
	private void populateClientInfo(Collection<?> collection) {
		if ( ! CollectionUtils.isEmpty(collection) ) {
			for (Object object : collection) {
				populateClientInfo(object);
			}
		}
	}

	private void populateClientInfo(Map<?,?> map) {
		if ( ! CollectionUtils.isEmpty(map) ) {
			populateClientInfo(map.values());
		}
	}

}
