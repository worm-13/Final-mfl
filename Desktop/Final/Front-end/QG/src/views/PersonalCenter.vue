<template>
  <div class="personal-center">
    <header class="header">
      <button class="back-btn" @click="goBack">← 返回</button>
      <h1>个人中心</h1>
    </header>

    <div class="profile-card">
      <div class="avatar">👤</div>
      <h2>{{ userStore.userInfo.nickname }}</h2>
    </div>

    <div class="stats">
      <div class="stat-item">
        <span class="stat-value">{{ userStore.userInfo.session }}</span>
        <span class="stat-label">总对局</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ userStore.userInfo.wins }}</span>
        <span class="stat-label">胜场</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ userStore.userInfo.score }}</span>
        <span class="stat-label">积分</span>
      </div>
    </div>

    <div class="menu">
      <button class="menu-item" @click="showHistory">
        📜 历史对局
      </button>
      <button class="menu-item" @click="showSettings">
        ⚙️ 设置
      </button>
      <button class="menu-item" @click="logout">
        🚪 退出登录
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();

onMounted(() => {
  userStore.initFromLocalStorage();
});

const goBack = () => {
  router.go(-1);
};

const showHistory = () => {
  router.push('/history')
};

const showSettings = () => {
  alert('显示设置');
};

const logout = () => {
  userStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.personal-center {
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

.profile-card {
  text-align: center;
  margin-bottom: 30px;
}

.avatar {
  font-size: 60px;
  margin-bottom: 15px;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30px;
  padding: 20px;
  background-color: greenyellow;
  border-radius: 10px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.menu-item {
  display: block;
  width: 100%;
  padding: 15px;
  margin-bottom: 10px;
  text-align: left;
  background: none;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.menu-item:hover {
  background-color: #f9f9f9;
}
</style>