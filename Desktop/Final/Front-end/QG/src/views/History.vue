<template>
    <div class="history-container">
      <header class="header">
        <button class="back-btn" @click="goBack">← 返回</button>
        <h1>历史对战记录</h1>
      </header>
  
      <div class="history-list">
        <div v-for="(record, index) in historyRecords" :key="index" class="history-record">
          <div class="record-header">
            <p>对局时间: {{ record.time }}</p>
            <p>胜者: {{ record.winner }}</p>
          </div>
          <div class="record-details">
            <p>黑方: {{ record.black }}</p>
            <p>白方: {{ record.white }}</p>
          </div>
          <div class="chess-board">
            <div v-for="(row, rowIndex) in record.border" :key="rowIndex" class="board-row">
              <div
                v-for="(cell, cellIndex) in row"
                :key="cellIndex"
                class="board-cell"
                :class="{ 'black-stone': cell === 1, 'white-stone': cell === 2 }"
              >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import axios from 'axios';
  
  const router = useRouter();
  const historyRecords = ref([]);
  
  onMounted(() => {
    fetchHistoryRecords();
  });
  
  const fetchHistoryRecords = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/history`);
        historyRecords.value = response.data;
    } catch (error) {
      console.error('获取历史记录失败:', error);
    }
  };
  
  const goBack = () => {
    router.go(-1);
  };
  </script>
  
  <style scoped>
  .history-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .back-btn {
    background: none;
    border: none;
    font-size: 18px;
    margin-right: 15px;
    cursor: pointer;
  }
  
  .history-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .history-record {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 15px;
    background-color: #f9f9f9;
  }
  
  .record-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
  }
  
  .record-details {
    margin-bottom: 10px;
  }
  
  .chess-board {
    background: #d4aa63;
    padding: 10px;
    border-radius: 5px;
  }
  
  .board-row {
    display: flex;
  }
  
  .board-cell {
    width: 30px;
    height: 30px;
    border: 1px solid #c4a46b;
  }
  
  .black-stone::after,
  .white-stone::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 20px;
    height: 20px;
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