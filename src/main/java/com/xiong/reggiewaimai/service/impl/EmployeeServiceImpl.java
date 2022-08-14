package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.Employee;
import com.xiong.reggiewaimai.service.EmployeeService;
import com.xiong.reggiewaimai.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LENOVO
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2022-08-10 15:15:39
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
}




