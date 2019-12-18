package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-18.
 */
@Slf4j
public class RemoteHttpTraceRepository extends InMemoryHttpTraceRepository {

    private static final String ACTUATOR = "actuator/";
    private static final String WEBSOCKET = "websocket/";

    @Override
    public void add(HttpTrace trace) {
        // 忽略 actuator/websocket 请求
        String path = trace.getRequest().getUri().getPath();
        if (path.contains(ACTUATOR) || path.contains(WEBSOCKET)) {
            return;
        }

        super.add(trace);
//         log.info(JSON.toJSONString(trace));
    }
}
