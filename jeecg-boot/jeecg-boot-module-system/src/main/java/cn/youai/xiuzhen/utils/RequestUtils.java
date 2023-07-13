package cn.youai.xiuzhen.utils;

import cn.youai.server.springboot.component.OkHttpHelper;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

@Slf4j
public final class RequestUtils {

    private RequestUtils() {
    }

    @SuppressWarnings("DuplicatedCode")
    public static <T, K> Map<Integer, T> batchGet(Collection<Integer> ids,
                                                  String path,
                                                  IService<K> service,
                                                  Function<K, String> gmFun,
                                                  Function<K, Boolean> skipFun,
                                                  Class<T> clazz) {
        Map<Integer, T> responseMap = new HashMap<>(ids.size());
        CountDownLatch latch = new CountDownLatch(ids.size());
        for (Integer serverId : ids) {
            K entity = service.getById(serverId);
            if (entity == null) {
                latch.countDown();
                continue;
            }

            if (skipFun != null && skipFun.apply(entity)) {
                latch.countDown();
                continue;
            }

            String gmUrl = gmFun.apply(entity) + path;
            OkHttpHelper.getAsync(gmUrl, new Callback() {
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error("batchGet url failed, url:" + gmUrl, e);
                    latch.countDown();
                }

                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            T rspObj = JSON.parseObject(response.body().string(), clazz);
                            responseMap.put(serverId, rspObj);
                        } catch (Exception e) {
                            log.error("batchGet error, serverId:" + serverId + ", path:" + path, e);
                        } finally {
                            Objects.requireNonNull(response.body()).close();
                        }
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("batchGet error, serverIds:" + ids + ", path:" + path, e);
        }
        return responseMap;
    }

    @SuppressWarnings("DuplicatedCode")
    public static <T, K> Map<Integer, T> batchGet(Collection<Integer> ids,
                                                  String path,
                                                  Map<String, Object> params,
                                                  IService<K> service,
                                                  Function<K, String> gmFun,
                                                  Function<K, Boolean> skipFun,
                                                  Class<T> clazz) {
        Map<Integer, T> responseMap = new ConcurrentHashMap<>(ids.size());
        CountDownLatch latch = new CountDownLatch(ids.size());
        for (Integer id : ids) {
            K entity = service.getById(id);
            if (entity == null) {
                latch.countDown();
                continue;
            }

            if (skipFun != null && skipFun.apply(entity)) {
                latch.countDown();
                continue;
            }

            String gmUrl = gmFun.apply(entity) + path;
            OkHttpHelper.getAsync(gmUrl, params, new Callback() {
                public void onFailure(@NotNull Call call, IOException e) {
                    log.error("batchGet url failed, url:" + gmUrl, e);
                    latch.countDown();
                }

                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            responseMap.put(id, JSON.parseObject(response.body().string(), clazz));
                        } finally {
                            Objects.requireNonNull(response.body()).close();
                        }
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("batchGet error, serverIds:" + ids + ", path:" + path, e);
        }
        return responseMap;
    }

    @SuppressWarnings("DuplicatedCode")
    public static <T, K> Map<Integer, T> batchPost(Collection<Integer> ids,
                                                   String path, Object data,
                                                   IService<K> service,
                                                   Function<K, String> gmFun,
                                                   Function<K, Boolean> skipFun,
                                                   Class<T> clazz) {
        Map<Integer, T> responseMap = new HashMap<>(ids.size());
        CountDownLatch latch = new CountDownLatch(ids.size());
        for (Integer id : ids) {
            K entity = service.getById(id);
            if (entity == null) {
                latch.countDown();
                continue;
            }

            if (skipFun != null && skipFun.apply(entity)) {
                latch.countDown();
                continue;
            }

            String gmUrl = gmFun.apply(entity) + path;
            OkHttpHelper.postAsync(gmUrl, data, new Callback() {
                public void onFailure(@NotNull Call call, IOException e) {
                    log.error("batchPost url failed, url:" + gmUrl, e);
                    latch.countDown();
                }

                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            responseMap.put(id, JSON.parseObject(response.body().string(), clazz));
                        } finally {
                            Objects.requireNonNull(response.body()).close();
                        }
                    }
                    latch.countDown();
                }
            });

        }
        return responseMap;
    }

}
