package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.SkuSaleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GmallSmsApi {


        @PostMapping("sms/skubounds/sales")
        public ResponseVo<Object> saveSales(@RequestBody SkuSaleVO skuSaleVo);

}