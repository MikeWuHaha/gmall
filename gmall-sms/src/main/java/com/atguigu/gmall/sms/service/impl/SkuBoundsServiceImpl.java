package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SkuLadderMapper;
import com.atguigu.gmall.sms.vo.SkuSaleVO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.sms.mapper.SkuBoundsMapper;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsMapper, SkuBoundsEntity> implements SkuBoundsService {

      // 打折的mapper
    @Autowired
    private SkuLadderMapper  skuLadderMapper;


        // 满减的mapper
    @Autowired
    private SkuFullReductionMapper reductionMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SkuBoundsEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    @Transactional
    public void saveSales(SkuSaleVO skuSaleVo) {

        // 3.1保sms_sku_bounds
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        BeanUtils.copyProperties(skuSaleVo,skuBoundsEntity);
        List<Integer> works = skuSaleVo.getWork();
        if (!CollectionUtils.isEmpty(works) && works.size() ==4){
            skuBoundsEntity.setWork(works.get(3) * 8 +works.get(2)*4 + works.get(1)*2+ works.get(0));
        }

        this.save(skuBoundsEntity);


        //3.2保存sms_sku_reduction

        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuSaleVo,skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuSaleVo.getFullAddOther());

        this.reductionMapper.insert(skuFullReductionEntity);
        // 3.3保存sms_sku_ladder

        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuSaleVo,skuLadderEntity);
        skuLadderEntity.setAddOther(skuSaleVo.getLadderAddOther());
        this.skuLadderMapper.insert(skuLadderEntity);
    }

}