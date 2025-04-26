<template>
    <div class="settings-container">
      <header class="header">
        <button class="back-btn" @click="goBack">← 返回</button>
        <h1>修改个人信息</h1>
      </header>
  
      <form @submit.prevent="updateUserInfo" class="settings-form">
        <div class="form-group">
          <label for="nickname">昵称</label>
          <input id="nickname" v-model="userStore.userInfo?.nickname" type="text" required />
        </div>
  
        <div class="form-group">
          <label for="phone">手机号</label>
          <input id="phone" v-model="userStore.userInfo?.phoneNumber" type="text" required />
        </div>
  
        <div class="form-group">
          <label for="email">邮箱</label>
          <input id="email" v-model="userStore.userInfo?.email" type="email" required />
        </div>
  
        <div class="form-group">
          <label for="password">密码</label>
          <input id="password" v-model="newPassword" type="password" />
        </div>
  
        <button type="submit" class="submit-btn">保存修改</button>
      </form>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { useUserStore } from '../stores/user';
  import axios from 'axios';
  
  const router = useRouter();
  const userStore = useUserStore();
  const newPassword = ref('');
  
  const goBack = () => {
    router.go(-1);
  };
  
  const updateUserInfo = async () => {
    try {
      // 创建一个包含新信息的对象
      const updatedInfo = {
        nickname: userStore.userInfo?.nickname,
        phone: userStore.userInfo?.phoneNumber,
        email: userStore.userInfo?.email,
        password: newPassword.value
      };
  
      // 发送请求到后端更新用户信息
      await axios.post('http://localhost:8080/api/update-profile', updatedInfo);
  
      // 更新本地存储和 Pinia store
      userStore.updateUserInfo({
        nickname: userStore.userInfo?.nickname,
        phone: userStore.userInfo?.phoneNumber,
        email: userStore.userInfo?.email
      });
  
      // 返回个人中心页面
      router.push('/personal-center');
    } catch (error) {
      console.error('更新个人信息失败:', error);
      alert('更新个人信息失败');
    }
  };
  </script>
  
  <style scoped>
  .settings-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .header {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
  }
  
  .back-btn {
    background: none;
    border: none;
    font-size: 18px;
    margin-right: 15px;
    cursor: pointer;
  }
  
  .settings-form {
    background-color: #f9f9f9;
    padding: 20px;
    border-radius: 10px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 5px;
  }
  
  .form-group input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .submit-btn {
    padding: 10px 15px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    width: 100%;
  }
  
  .submit-btn:hover {
    background-color: #45a049;
  }
  </style>