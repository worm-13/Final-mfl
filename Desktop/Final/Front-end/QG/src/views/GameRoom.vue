<template>
  <div class="game-room">
    <div class="game-status">
      <span class="room-id" style="color: black;">房间号: {{ opponentInfo.roomId }}</span>
      <span style="color: aqua;">你是{{ opponentInfo.role }} </span>
      <span class="nickname" style="color: black;">对手: {{ opponentInfo.nickname }}</span>
      <span v-if="currentTurn === 'B'" style="color: red;">黑棋回合</span>
      <span v-else style="color: blue;">白棋回合</span>
      <span v-if="isYourTurn" style="color: green;">轮到你落子</span>
      <span v-else style="color: gray;">等待对方落子</span>
    </div>
    <div class="chess-board">
      <div v-for="(row, rowIndex) in board" :key="rowIndex" class="board-row">
        <div
          v-for="(cell, cellIndex) in row"
          :key="cellIndex"
          class="board-cell"
          :class="{ 'black-stone': cell === 'B', 'white-stone': cell === 'W' }"
          @click="handleCellClick(rowIndex, cellIndex)"
        >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';
import Swal from 'sweetalert2';

const router = useRouter();
const opponentInfo = ref({ nickname: '', score: 0, roomId: 0, role: '' });
const userStore = useUserStore();
const board = ref(Array(15).fill().map(() => Array(15).fill('')));
const currentTurn = ref('B'); // 当前回合，'B' 代表黑棋，'W' 代表白棋
const isYourTurn = ref(false); // 是否是当前玩家的回合
let websocket: WebSocket | null = null;

onMounted(() => {
  const storedOpponentInfo = localStorage.getItem('opponentInfo');
  if (storedOpponentInfo) {
    opponentInfo.value = JSON.parse(storedOpponentInfo);
  }
  initWebSocket();
});

// 初始化 WebSocket 连接
const initWebSocket = () => {
  websocket = new WebSocket('ws://localhost:8080/system_war_exploded/gaming');

  websocket.onopen = () => {
    setTimeout(() => {
      const start={type:'PLAYER_INFO',roomId:opponentInfo.value.roomId,nickname:userStore.userInfo?.nickname};
      websocket?.send(JSON.stringify(start))},100);

    console.log('WebSocket connection established');
    // 初始化回合状态，黑棋先手
    currentTurn.value = 'B';
    // 检查当前玩家是否是黑棋，以确定是否是自己的回合
    isYourTurn.value = opponentInfo.value.role === '黑棋';
  };

  websocket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    if(data.type==='GAME_OVER'){
      setTimeout(() => {
      Swal.fire({
        title: '游戏结束',
        html: `
          <div style="font-size: 1.2rem; margin-bottom: 1rem;">
            ${data.winner} 获胜！
          </div>
          `,
        confirmButtonText: '确定',
        allowOutsideClick: false,
        allowEscapeKey: false
      }).then(() => {
        router.go(-2);
      });
    }, 100);
      if(data.winner===userStore.userInfo?.nickname){
        // 在这里判断是否加分
      }
    }
    console.log(data.winner)
    handleServerMessage(data);
  };

  websocket.onclose = () => {
    console.log('WebSocket connection closed');
  };

  websocket.onerror = (event) => {
    console.error('WebSocket error:', event);
  };
};

// 处理服务器消息
const handleServerMessage = (data: any) => {
  switch (data.type) {
    case 'MOVE_CONFIRM':
      if (data.row !== undefined && data.col !== undefined) {
        board.value[data.row][data.col] = data.player;
        currentTurn.value = currentTurn.value === 'B' ? 'W' : 'B';
        isYourTurn.value = currentTurn.value === (opponentInfo.value.role === '黑棋' ? 'B' : 'W');
      }
      break;
    case 'TURN_CHANGE':
      currentTurn.value = data.player;
      isYourTurn.value = currentTurn.value === (opponentInfo.value.role === '黑棋' ? 'B' : 'W');
      break;
    
      
      break;
    default:
      console.log('未知的消息类型:', data.type);
  }
};

onUnmounted(() => {
  // 关闭 WebSocket 连接
  if (websocket) {
    websocket.close();
  }
});

const handleCellClick = (rowIndex: number, cellIndex: number) => {
  console.log(`Clicked cell at row ${rowIndex + 1}, column ${String.fromCharCode(65 + cellIndex)}`);

  // 检查当前格子是否为空
  if (board.value[rowIndex][cellIndex] === '') {
    // 检查是否是当前玩家的回合并且是否是正确的棋子方
    if (isYourTurn.value && ((opponentInfo.value.role === '黑棋' && currentTurn.value === 'B') || (opponentInfo.value.role === '白棋' && currentTurn.value === 'W'))) {
      // 发送落子信息到后端
      if (websocket && websocket.readyState === WebSocket.OPEN) {
        const moveMessage = {
          type: 'MOVE',
          roomId: opponentInfo.value.roomId,
          row: rowIndex,
          col: cellIndex,
          player: opponentInfo.value.role === '黑棋' ? 'B' : 'W'
        };
        websocket.send(JSON.stringify(moveMessage));
      }
    } else {
      console.log('不是你的回合或不是你的棋子方');
    }
  } else {
    console.log('该位置已有棋子');
  }
};
</script>

<style scoped>
.game-room {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.game-status {
  margin-bottom: 20px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
}

.chess-board {
  background: #d4aa63;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.board-row {
  display: flex;
}

.board-cell {
  width: 40px;
  height: 40px;
  border: 1px solid #c4a46b;
  position: relative;
  transition: background 0.2s;
  cursor: pointer; /* 添加指针样式，表示可点击 */
}

.board-cell:hover {
  background: #e8c79f;
}

.black-stone::after,
.white-stone::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.black-stone::after {
  background: #333;
}

.white-stone::after {
  background: #fff;
  border: 1px solid #ddd;
}
</style>