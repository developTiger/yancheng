package com.sunesoft.seera.yc.core.coupon.application.factory;

import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.fr.utils.excel.ReadExcel;
import com.sunesoft.seera.yc.core.coupon.application.dto.UploadCoupon;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * Created by zwork on 2016/11/14.
 */
public class CouponUploadFactory extends ReadExcel<UploadCoupon> {

        @Override
        protected UniqueResult<UploadCoupon> convertRow(HSSFRow row, int rowNum, int colNum) {
            String value;
            try {
                UploadCoupon dto = new UploadCoupon();
                if (!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(0))))
                    dto.setTuName(getCellFormatValue(row.getCell(0)));
                if (!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(1))))
                    dto.setPhoneNo(getCellFormatValue(row.getCell(1)));
                if (!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(2))))
                    dto.setCouponCode((long) Float.parseFloat(getCellFormatValue(row.getCell(2))));
                if (!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(3))))
                    dto.setCouponCount((int)Float.parseFloat(getCellFormatValue(row.getCell(3))));
                if (!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(4))))
                    dto.setEndDate(DateHelper.parse(getCellFormatValue(row.getCell(4)),"yyyy-MM-dd"));
                return new UniqueResult<UploadCoupon>(dto);
            } catch (Exception ex) {
                return new UniqueResult<UploadCoupon>("行" + rowNum + ",数据解析错误，请检查！");
            }
        }

        /**
         * 判断表格是否正确
         *
         * @param row 表头信息(列名)
         * @return Boolean
         */
        protected Boolean checkExcelCol(HSSFRow row) {

            //可判断指定列名是否一致等，如返回false 则会报格式不正确的错误
            if (row.getLastCellNum() !=5)
                return false;
            return true;
        }

}
