package com.msb.k01Prm.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MyBatis-Plus 代码生成器
 * 
 * 使用方式：
 * 1. 修改下面的数据库连接信息
 * 2. 修改 outputDir（代码输出目录）
 * 3. 修改 packageConfig（包名）
 * 4. 修改 strategyConfig（表名、字段过滤等）
 * 5. 运行 main 方法即可生成代码
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 数据库连接配置
        String url = "jdbc:mysql://localhost:3306/sirdb?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
        String username = "root";
        String password = "sirroot7&";
        
        // 代码输出目录（建议使用绝对路径）
        String outputDir = System.getProperty("user.dir") + "/src/main/java";
        
        // 作者名
        String author = "GJJ";
        
        // 父包名
        String parentPackage = "com.msb.k01Prm";
        
        // 要生成的表名（多个表用逗号分隔，为空则生成所有表）
        String[] tables = {
                "myjk_plant_action"
                // "product_group",  // 示例：生成 product_group 表的代码
                // "user",           // 示例：生成 user 表的代码
        };
        
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .outputDir(outputDir) // 指定输出目录
                            .disableOpenDir() // 禁止打开输出目录
                            .commentDate("yyyy-MM-dd HH:mm:ss"); // 注释日期格式
                })
                .packageConfig(builder -> {
                    builder.parent(parentPackage) // 设置父包名
                            .moduleName("") // 设置父包模块名（可选，如果为空则不创建模块目录）
                            .pathInfo(Collections.singletonMap(
                                    com.baomidou.mybatisplus.generator.config.OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(getTables(tables)) // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_") // 设置过滤表前缀（可选）
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service 文件名格式
                            .formatServiceImplFileName("%sServiceImpl") // ServiceImpl 文件名格式
                            .entityBuilder()
                            .enableLombok() // 开启 Lombok
                            .enableFileOverride() // 覆盖已生成文件
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE)
                            ) // 自动填充字段（可选）
                            .controllerBuilder()
                            .enableRestStyle() // 开启生成 @RestController 控制器
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启 @Mapper 注解
                            .enableBaseResultMap() // 启用 BaseResultMap 生成
                            .enableBaseColumnList(); // 启用 BaseColumnList 生成
                })
                .execute();
    }
    
    /**
     * 处理表名，支持多表生成
     */
    protected static List<String> getTables(String... tables) {
        if (tables == null || tables.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(tables);
    }
}
