# Spring底层

## 1、容器接口

### 1.1、BeanFactory能做什么

> 什么是BeanFactory
>
> - 它是ApplicationContext的父接口
> - 它才是Spring的核心容器，主要的ApplicationContext实现都【结合】了它的功能

> BeanFactory能做什么
>
> - 表面上只有getBean
> - 实际上控制反转、基本的依赖注入、直至Bean的生命周期的各种功能，都是由它的实现类提供

### 1.2、ApplicationContext有哪些扩展功能

- 国际化
- 通配符获取资源
- 获取配置信息
- 事件发布



## 2、容器实现

### 2.1、BeanFactory实现的特点

### 2.2、ApplicationContext的常见实现和用法

### 2.3、内嵌容器、注册DispatcherServlet