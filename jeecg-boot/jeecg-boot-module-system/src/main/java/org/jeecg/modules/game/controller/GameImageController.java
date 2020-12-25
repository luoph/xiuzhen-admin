package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.modules.game.entity.GameImage;
import org.jeecg.modules.game.service.IGameImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏图片
 * @date 2020-11-02
 */
@Slf4j
@RestController
@RequestMapping("game/gameImage")
public class GameImageController extends JeecgController<GameImage, IGameImageService> {

    @Value(value = "${app.folder.image}")
    private String uploadPath;

    @Autowired
    private IGameImageService gameImageService;

    public static void main(String[] args) {
        String name = "/Users/luopeihuan/workspace/gitlab/xiuzhen-admin/jeecg-boot/image.png";
//        FileUtil.mainName(name);
        System.out.println(FileUtil.mainName(name));
    }

    /**
     * 分页列表查询
     *
     * @param gameImage 数据实体
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param req       请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameImage gameImage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameImage> queryWrapper = QueryGenerator.initQueryWrapper(gameImage, req.getParameterMap());
        Page<GameImage> page = new Page<>(pageNo, pageSize);
        IPage<GameImage> pageList = gameImageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameImage 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameImage gameImage) {
        gameImageService.save(gameImage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameImage 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameImage gameImage) {
        gameImageService.updateById(gameImage);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameImageService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameImageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏图片-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameImage gameImage = gameImageService.getById(id);
        if (gameImage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameImage);
    }

    /**
     * 导出excel
     *
     * @param request   请求
     * @param gameImage 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameImage gameImage) {
        return super.exportXls(request, gameImage, GameImage.class, "游戏图片");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameImage.class);
    }

    @PostMapping(value = "/upload")
    public Result<?> upload(HttpServletRequest request, HttpServletResponse response) {
        Result<GameImage> result = new Result<>();
        try {
            Date now = new Date();
            String today = DateUtil.format(now, "yyyyMMdd");
            String imageFolder = uploadPath + File.separator + today;

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传文件对象
            MultipartFile mf = multipartRequest.getFile("file");
            if (mf == null) {
                return Result.error("上传错误");
            }

            // 获取文件名
            String orgName = mf.getOriginalFilename();
            String time = DateUtil.format(now, "HHmmss");
            String newName = FileUtil.mainName(orgName) + "_" + time + "." + FileUtil.extName(orgName);
            String savePath = imageFolder + File.separator + newName;
            // 创建文件根目录
            FileUtil.mkParentDirs(savePath);

            File saveFile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), saveFile);

            BufferedImage bufferedImage = ImageIO.read(saveFile);
            String dbpath = FileUtil.getName(uploadPath) + File.separator + today + File.separator + newName;
            dbpath = dbpath.replace("\\", "/");

            GameImage gameImage = new GameImage()
                    .setImgUrl(dbpath).setName(newName)
                    .setWidth(bufferedImage.getWidth())
                    .setHeight(bufferedImage.getHeight());

            result.setResult(gameImage);
            result.setSuccess(true);
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 预览图片
     */
    @GetMapping(value = "/view/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String imgPath = BrowserUtils.extractPathFromPattern(request);
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            imgPath = imgPath.replace("..", "");
            if (imgPath.endsWith(",")) {
                imgPath = imgPath.substring(0, imgPath.length() - 1);
            }
            response.setContentType("image/jpeg;charset=utf-8");
            String imgUrl = FileUtil.getName(uploadPath) + File.separator + imgPath;
            inputStream = new BufferedInputStream(new FileInputStream(imgUrl));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览图片失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
