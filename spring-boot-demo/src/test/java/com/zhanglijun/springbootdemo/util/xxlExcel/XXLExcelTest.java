package com.zhanglijun.springbootdemo.util.xxlExcel;

import com.google.common.collect.Lists;
import com.xuxueli.poi.excel.ExcelExportUtil;
import com.zhanglijun.springbootdemo.domain.xxlexcel.ShopDTO;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author 夸克
 * @date 2018/9/25 11:19
 */
public class XXLExcelTest {

    @Test
    public void testExcelExport() {
        /**
         * mock数据，Java对象列表
         */
        List<ShopDTO> shopDTOList = Lists.newArrayList();

        for (int i=0; i < 100; i++) {
            ShopDTO shopDTO = new ShopDTO(true, "商户" + i, (short) i, 1000+i, (long)10000+i,
                    (float) 1000+i, (double)1000+i, new Date());
            shopDTOList.add(shopDTO);
        }

        String filePath = "/Users/zhanglijun/Documents/shop.csv";
        // 导出Excel
        ExcelExportUtil.exportToFile(filePath, shopDTOList);

    }
}
