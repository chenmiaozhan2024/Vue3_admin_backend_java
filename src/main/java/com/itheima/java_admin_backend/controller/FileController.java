package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件上传（品牌 logo 等图片，路径与 Node 版保持一致：
 * 保存到 upload/img/sph/yyyyMMdd/，返回 /api/static/img/sph/yyyyMMdd/文件名）
 */
@RestController
public class FileController {

    @PostMapping("/admin/product/fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        try {
            // 原始文件名，去掉路径分隔符防止目录穿越
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                return Result.error("文件名不能为空");
            }
            originalName = originalName.replace("/", "_").replace("\\", "_");

            // 按日期分目录：upload/img/sph/20250208
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dir = System.getProperty("user.dir") + "/upload/img/sph/" + date;
            Path uploadDir = Paths.get(dir);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            file.transferTo(uploadDir.resolve(originalName).toFile());

            // 返回可直接访问的图片 URL（由 WebConfig 中的静态资源映射提供访问）
            String imgUrl = "/api/static/img/sph/" + date + "/" + originalName;
            return Result.success(imgUrl);
        } catch (IOException e) {
            return Result.error("文件上传失败");
        }
    }
}
