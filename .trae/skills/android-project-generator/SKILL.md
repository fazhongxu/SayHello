---
name: android-project-generator
description: 生成Android项目基础结构、代码模板和UI设计辅助。在创建新项目、添加新功能、遇到开发问题或进行代码优化时调用。
---

# Android项目生成器

## 架构说明

本工具生成的项目采用MVVM（Model-View-ViewModel）架构，具有以下特点：

- **Model**：数据模型层，负责数据的获取和存储
- **View**：视图层，包括Activity和Fragment，负责UI展示
- **ViewModel**：业务逻辑层，连接View和Model，处理业务逻辑
- **Repository**：数据访问层，负责数据的来源和管理

## 功能介绍

- **项目初始化**：生成完整的Android MVVM项目基础结构，包括必要的配置文件和目录结构
- **代码生成**：根据需求生成常用的Android MVVM代码模板，如Activity、Fragment、ViewModel、Repository、Adapter等
- **UI设计辅助**：提供Android UI构建的指导和代码示例
- **开发问题解决**：提供常见Android开发问题的解决方案
- **代码优化**：分析并提供代码优化建议

## 调用场景

- **创建新项目时**：生成完整的项目结构和初始配置
- **添加新功能时**：生成相关功能的代码模板
- **遇到开发问题时**：提供问题分析和解决方案
- **进行代码优化时**：分析代码并提供优化建议

## 使用示例

### 1. 创建新项目

```bash
# 示例命令
android-project-generator create --name MyApp --package com.example.myapp --min-sdk 21 --target-sdk 34
```

### 2. 生成代码模板

```bash
# 生成Activity模板
android-project-generator generate activity --name MainActivity --layout main_activity

# 生成Fragment模板
android-project-generator generate fragment --name HomeFragment --layout fragment_home

# 生成ViewModel模板
android-project-generator generate viewmodel --name MainViewModel --model User

# 生成Repository模板
android-project-generator generate repository --name UserRepository --model User

# 生成Adapter模板
android-project-generator generate adapter --name ItemAdapter --model Item
```

### 3. UI设计辅助

```bash
# 生成RecyclerView布局
android-project-generator generate ui recyclerview --name items_list

# 生成Material Design组件
android-project-generator generate ui material --component button
```

### 4. 问题解决

```bash
# 分析编译错误
android-project-generator analyze error --message "Could not resolve com.android.support:appcompat-v7"

# 提供性能优化建议
android-project-generator optimize performance --target activity
```

## 配置选项

- `--name`：项目或组件名称
- `--package`：项目包名
- `--min-sdk`：最低SDK版本
- `--target-sdk`：目标SDK版本
- `--layout`：布局文件名称
- `--model`：数据模型类名称

## 支持的模板类型

- Activity
- Fragment
- ViewModel
- Repository
- Adapter
- Service
- BroadcastReceiver
- ContentProvider
- RecyclerView
- ListView
- Material Design组件

## 注意事项

- 确保Android Studio已正确安装并配置
- 生成的代码可能需要根据具体需求进行调整
- 对于复杂项目，建议先了解项目结构后再使用此工具

##支持的语言 

-java，kotlin
-优先使用java，没我的指示不要优先kotlin

## 示例项目结构

```
MyApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/myapp/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── activities/
│   │   │   │   │   └── fragments/
│   │   │   │   ├── viewmodels/
│   │   │   │   ├── repositories/
│   │   │   │   ├── models/
│   │   │   │   ├── utils/
│   │   │   │   └── services/
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
├── build.gradle
└── settings.gradle
```