package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/2 14:22
 */
@Service
public class ConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    // 根据id查询路径
    public CmsConfig find (String id){

        if(id != null){
            Optional<CmsConfig> byId = cmsConfigRepository.findById(id);
            if(byId.isPresent()){
                CmsConfig cmsConfig = byId.get();
                return cmsConfig;
            }
        }
        return null;
    }
}
