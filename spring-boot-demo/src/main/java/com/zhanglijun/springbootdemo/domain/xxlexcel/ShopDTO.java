package com.zhanglijun.springbootdemo.domain.xxlexcel;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.Date;

/**
 * excel导出实体
 * @author 夸克
 * @date 2018/9/25 11:20
 */
@ExcelSheet(name = "商户列表",headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
@Data
@AllArgsConstructor
public class ShopDTO {

    @ExcelField(name = "是否VIP商户")
    private Boolean vip;

    @ExcelField(name = "商户名称")
    private String shopName;

    @ExcelField(name = "分店数量")
    private Short branchNum;

    @ExcelField(name = "商户ID")
    private Integer shopId;

    @ExcelField(name = "浏览人数")
    private Long visitNum;

    @ExcelField(name = "当月营业额")
    private Float turnover;

    @ExcelField(name = "历史营业额")
    private Double totalTurnover;

    @ExcelField(name = "开店时间")
    private Date addTime;

}
