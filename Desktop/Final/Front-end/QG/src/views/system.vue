<template>
  <div class="system-container">
    <header class="header">
      <h1>QG五子棋对战平台</h1>
      <br>
      <button class="user-btn" @click="navigateToPersonalCenter">
        <span class="username">{{ username }}</span>
        <span class="score">总积分: {{ userStore.userInfo?.score || 0 }}</span>
        <span class="rank">段位: 最强王者</span>
        <i class="icon">👤</i>
      </button>
    </header>

    <main class="main-content">
      <div class="button-grid">
        <div class="button-row">
          <button 
            class="match-btn" 
            @click="startMatchmaking" 
            
          >
            🎮 开始匹配
          </button>
          <button class="friend-btn" @click="playWithFriend">
            👥 好友对战
          </button>
          <button class="ai-btn" @click="playWithAI">
            🤖 AI对战
          </button>
        </div>
        <div class="button-row">
          <button class="friend-btn" @click="friendcheck">
            🚶 好友列表
          </button>
          <button class="shop-btn" @click="shopping">
            🏬 游戏商城
          </button>
          <button class="match-btn" @click="learnAbout">
            🙂 关于我们
          </button>
        </div>
      </div>
    </main>

    

      
  </div>
  
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();
const username = computed(() => userStore.userInfo?.nickname || '玩家');


// 导航到个人中心
const navigateToPersonalCenter = () => {
  router.push('/personal-center');
};

// 开始匹配
const startMatchmaking = () => {
  router.push('/matching')
};


const playWithFriend = () => {
  
};

const playWithAI = () => {
  
};

const friendcheck = () => {
  
};

const shopping = () => {
  
};

const learnAbout = () => {
  
};
</script>

<style scoped>
.system-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  height: 200px;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: 1px solid #ddd;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
}

.button-grid {
  display: flex;
  flex-direction: column;
  gap: 20px; /* 控制两行按钮之间的间距 */
}

.button-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px; /* 控制按钮之间的间距 */
}

.button-row button {
  padding: 15px;
  font-size: 18px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s;
  width: 100%; /* 确保按钮填满网格单元格 */
}

.button-row button:hover {
  transform: translateY(-3px);
}

.match-btn {
  background-color: #4CAF50;
  color: white;
}

.friend-btn {
  background-color: #2196F3;
  color: white;
}

.ai-btn {
  background-color: #9C27B0;
  color: white;
}

.shop-btn {
  background-color: blueviolet;
  color: white;
}
</style>