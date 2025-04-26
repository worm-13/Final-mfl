<template>
  <div class="matching-container">
    <h2>正在寻找对手...</h2>
    <div class="loading-animation"></div>
    <button @click="cancelMatch">取消匹配</button>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();
let websocket: WebSocket | null = null;

onMounted(() => {
  websocket = new WebSocket('ws://localhost:8080/system_war_exploded/join-game');

  websocket.onopen = () => {
    const msg = {
      type: 'JOIN_QUEUE',
      nickname: userStore.userInfo?.nickname || '',
      score: userStore.userInfo?.score || 0,
    };
    websocket?.send(JSON.stringify(msg));
  };

  websocket.onmessage = (event) => {
    try {
        const data = JSON.parse(event.data);
        switch (data.type) {
            case 'JOIN_SUCCESS':
                // 发送自己的信息给后端
                const myInfo = {
                    type: 'PLAYER_INFO',
                    nickname: userStore.userInfo?.nickname,
                    score: userStore.userInfo?.score
                };
                websocket.send(JSON.stringify(myInfo));
                break;
            case 'OPPONENT_INFO':
                // 存储对手信息
                localStorage.setItem('opponentInfo', JSON.stringify(data.data));
                // 跳转到游戏房间
                setTimeout(() => {
                    router.push('/game-room');
                }, 1000); // 延迟跳转，确保数据已存储
                break;
            case 'ERROR':
                alert(data.message);
                router.back();
                break;
        }
    } catch (error) {
        console.error('解析消息失败:', error);
        // 可以在这里添加额外的错误处理逻辑
    }
};

  websocket.onclose = () => {
    console.log('WebSocket connection closed');
  };
});

onUnmounted(() => {
  websocket?.close();
});

const cancelMatch = () => {
  const msg = { type: 'LEAVE_QUEUE' };
  websocket?.send(JSON.stringify(msg));
  websocket?.close();
  router.back();
};
</script>

<style scoped>
.matching-container {
  text-align: center;
  padding: 2rem;
}

.loading-animation {
  margin: 2rem auto;
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #2980b9;
}
</style>