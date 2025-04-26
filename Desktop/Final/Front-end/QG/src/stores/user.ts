import { defineStore } from 'pinia';
import { ref } from 'vue';

interface UserInfo {
  nickname: string;
  phoneNumber: string;
  email: string;
  session: number;
  wins: number;
  score: number;
}

export const useUserStore = defineStore('user', () => {
  const isAuthenticated = ref(false);
  const userInfo = ref<UserInfo | null>(null);

  // ��localStorage��ʼ���û�״̬
  const initFromLocalStorage = () => {
    const auth = localStorage.getItem('isAuthenticated');
    const user = localStorage.getItem('userInfo');
    
    isAuthenticated.value = auth === 'true';
    if (user) {
      userInfo.value = JSON.parse(user);
    }
  };

  // ��¼�ɹ�ʱ����
  const loginSuccess = (userData: UserInfo) => {
    isAuthenticated.value = true;
    userInfo.value = userData;
    localStorage.setItem('isAuthenticated', 'true');
    localStorage.setItem('userInfo', JSON.stringify(userData));
  };

  // �˳���¼
  const logout = () => {
    isAuthenticated.value = false;
    userInfo.value = null;
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userInfo');
  };

  return {
    isAuthenticated,
    userInfo,
    initFromLocalStorage,
    loginSuccess,
    logout
  };
});