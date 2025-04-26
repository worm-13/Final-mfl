<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useUserStore } from '../stores/user';

// 状态管理
const isRegister = ref(false); // 是否显示注册界面
const isPhoneLogin = ref(true); // 是否是手机号登录
const registerForm = ref({
  nickname: '',
  password: '',
  phone: '',
  email: '',
});
const loginForm = ref({
  phone: '',
  email: '',
  password: '',
});
const errorMsg = ref(''); // 错误信息
const successMsg = ref(''); // 成功信息

// 监听登录方式切换，清空相关字段
watch(isPhoneLogin, (newVal) => {
  if (newVal) {
    loginForm.value.email = ''; // 切换到手机登录时清空邮箱
  } else {
    loginForm.value.phone = ''; // 切换到邮箱登录时清空手机号
  }
  errorMsg.value = ''; // 清空错误信息
});

// 后端 API 地址
const API_BASE_URL = 'http://localhost:8080/system_war_exploded';

// 获取 Vue Router 实例
const router = useRouter();

// 获取userstore实例
const userStore = useUserStore();

// 登录逻辑
const handleLogin = async () => {
  errorMsg.value = '';
  successMsg.value = '';

  // 根据登录类型进行验证
  if (isPhoneLogin.value) {
    // 手机号登录验证
    if (!loginForm.value.phone.trim()) {
      errorMsg.value = '手机号不能为空';
      return;
    }
    if (!/^[1][3-9][0-9]{9}$/.test(loginForm.value.phone)) {
      errorMsg.value = '手机号格式不正确';
      return;
    }
  } else {
    // 邮箱登录验证
    if (!loginForm.value.email.trim()) {
      errorMsg.value = '邮箱不能为空';
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(loginForm.value.email)) {
      errorMsg.value = '邮箱格式不正确';
      return;
    }
  }

  // 公共密码验证
  if (!loginForm.value.password.trim()) {
    errorMsg.value = '密码不能为空';
    return;
  }

  try {
    // 发送登录请求
    const response = await axios.post(`${API_BASE_URL}/login`, {
      phone: isPhoneLogin.value ? loginForm.value.phone : '',
      email: !isPhoneLogin.value ? loginForm.value.email : '',
      password: loginForm.value.password,
      isPhoneLogin: isPhoneLogin.value,
    }, {
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (response.status === 200) {
      // 保存后端发回的玩家信息
      const userData = response.data.data; 
      localStorage.setItem('isAuthenticated', 'true');
      userStore.loginSuccess({
        nickname: userData.nickname,
        phoneNumber: userData.phoneNumber,
        email: userData.email,
        session: userData.session,
        wins: userData.wins,
        score: userData.score
      });
      router.push('/system'); // 跳转到系统页面
    } else {
      errorMsg.value = '登录失败，请稍后再试';
    }
  } catch (error) {
    if (error.response && error.response.status === 401) {
      errorMsg.value = '账号或密码错误';
    } else {
      errorMsg.value = '网络错误，请稍后再试';
    }
    console.error('登录失败:', error);
  }
};

// 注册逻辑
const handleRegister = async () => {
  errorMsg.value = '';
  successMsg.value = '';

  // 验证昵称
  if (!registerForm.value.nickname.trim()) {
    errorMsg.value = '昵称不能为空';
    return;
  }

  // 验证密码
  if (!registerForm.value.password.trim()) {
    errorMsg.value = '密码不能为空';
    return;
  } else if (registerForm.value.password.length < 6) {
    errorMsg.value = '密码长度不能少于6位';
    return;
  }

  // 验证手机号和邮箱至少填写一个
  if (!registerForm.value.phone.trim() && !registerForm.value.email.trim()) {
    errorMsg.value = '手机号或邮箱至少填写一个';
    return;
  }

  // 验证手机号格式
  if (registerForm.value.phone.trim() && !/^[1][3-9][0-9]{9}$/.test(registerForm.value.phone)) {
    errorMsg.value = '手机号格式不正确';
    return;
  }

  // 验证邮箱格式
  if (registerForm.value.email.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(registerForm.value.email)) {
    errorMsg.value = '邮箱格式不正确';
    return;
  }

  try {
    // 发送注册请求
    const response = await axios.post(`${API_BASE_URL}/register`, {
      nickname: registerForm.value.nickname,
      password: registerForm.value.password,
      phone: registerForm.value.phone.trim() || '',
      email: registerForm.value.email.trim() || '',
    }, {
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (response.status === 201) {
      successMsg.value = '注册成功！请登录';
      registerForm.value = {
        nickname: '',
        password: '',
        phone: '',
        email: ''
      };
      setTimeout(() => {
        isRegister.value = false;
      }, 1500);
    } else {
      errorMsg.value = '注册失败，请稍后再试';
    }
  } catch (error) {
    if (error.response) {
      if (error.response.status === 400) {
        errorMsg.value = error.response.data.message || '注册信息有误';
      } else if (error.response.status === 409) {
        errorMsg.value = '手机号或邮箱已被注册';
      } else {
        errorMsg.value = '注册失败，请稍后再试';
      }
    } else {
      errorMsg.value = '网络错误，请检查连接';
    }
    console.error('注册失败:', error);
  }
};
</script>

<template>
  <div class="auth-container">
    <div class="auth-box">
      <h1 style="color: black;">QG五子棋对战平台</h1>
      <h2 v-if="!isRegister">登录</h2>
      <h2 v-else>注册</h2>

      <!-- 登录表单 -->
      <div v-if="!isRegister">
        <div class="login-type-switch">
          <button @click="isPhoneLogin = true" :class="{ active: isPhoneLogin }" style="color: aqua;">手机号登录</button>
          <button @click="isPhoneLogin = false" :class="{ active: !isPhoneLogin }" style="color: aqua;">邮箱登录</button>
        </div>

        <div class="form-group">
          <label>{{ isPhoneLogin ? '手机号' : '邮箱' }}</label>
          <input
            v-if="isPhoneLogin"
            type="text"
            v-model="loginForm.phone"
            :placeholder="isPhoneLogin ? '请输入手机号' : '请输入邮箱'"
          />
          <input
            v-else
            type="text"
            v-model="loginForm.email"
            :placeholder="isPhoneLogin ? '请输入手机号' : '请输入邮箱'"
          />
        </div>

        <div class="form-group">
          <label>密码</label>
          <input
            type="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
          />
        </div>

        <button class="submit-btn" @click="handleLogin">登录</button>
        <p class="error-msg" v-if="errorMsg">{{ errorMsg }}</p>
        <p class="success-msg" v-if="successMsg">{{ successMsg }}</p>
        <p class="switch-text" @click="isRegister = true">没有账号？去注册</p>
      </div>

      <!-- 注册表单 -->
      <div v-else>
        <div class="form-group">
          <label for="nickname">昵称</label>
          <input
            type="text"
            id="nickname"
            v-model="registerForm.nickname"
            placeholder="请输入昵称"
          />
        </div>

        <div class="form-group">
          <label for="register-password">密码</label>
          <input
            type="password"
            id="register-password"
            v-model="registerForm.password"
            placeholder="请输入密码(至少6位)"
          />
        </div>

        <div class="form-group">
          <label for="phone">手机号（可选）</label>
          <input
            type="text"
            id="phone"
            v-model="registerForm.phone"
            placeholder="请输入手机号"
          />
        </div>
        <div class="form-group">
          <label for="email">邮箱（可选）</label>
          <input
            type="text"
            id="email"
            v-model="registerForm.email"
            placeholder="请输入邮箱"
          />
        </div>

        <button class="submit-btn" @click="handleRegister">注册</button>
        <p class="error-msg" v-if="errorMsg">{{ errorMsg }}</p>
        <p class="success-msg" v-if="successMsg">{{ successMsg }}</p>
        <p class="switch-text" @click="isRegister = false">已有账号？去登录</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  width: 900px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  font-family: 'Arial', sans-serif;
}

.auth-box {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 450px;
  height: 600px;
  max-width: 90%;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
}

.login-type-switch {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

button.active {
  background-color: #42b983;
  color: white;
}

button {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #f5f5f5;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

input:focus {
  border-color: #42b983;
  outline: none;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #3aa870;
}

.error-msg {
  color: #e74c3c;
  margin-top: 10px;
  text-align: center;
}

.success-msg {
  color: #2ecc71;
  margin-top: 10px;
  text-align: center;
}

.switch-text {
  text-align: center;
  margin-top: 20px;
  color: #666;
  cursor: pointer;
}

.switch-text:hover {
  color: #42b983;
}
</style>