import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    nickname: '',
    score: 1000,
    isAuthenticated: false,
    phoneNumber: '',
    email: '',
    session: '',
    wins: 0
  }),
  actions: {
    // 登录成功后调用此方法
    loginSuccess(userInfo: {
      nickname: string;
      phoneNumber: string;
      email: string;
      session: string;
      wins: number;
      score: number;
    }) {
      this.nickname = userInfo.nickname;
      this.phoneNumber = userInfo.phoneNumber;
      this.email = userInfo.email;
      this.session = userInfo.session;
      this.wins = userInfo.wins;
      this.score = userInfo.score;
      this.isAuthenticated = true;

      // 将用户信息保存到 localStorage
      localStorage.setItem(
        'userInfo',
        JSON.stringify({
          nickname: this.nickname,
          score: this.score,
          isAuthenticated: this.isAuthenticated,
          phoneNumber: this.phoneNumber,
          email: this.email,
          session: this.session,
          wins: this.wins
        })
      );
    },

    // 从 localStorage 加载用户信息
    initFromLocalStorage() {
      const storedUserInfo = localStorage.getItem('userInfo');
      if (storedUserInfo) {
        const userInfo = JSON.parse(storedUserInfo);
        this.nickname = userInfo.nickname;
        this.score = userInfo.score;
        this.isAuthenticated = userInfo.isAuthenticated;
        this.phoneNumber = userInfo.phoneNumber;
        this.email = userInfo.email;
        this.session = userInfo.session;
        this.wins = userInfo.wins;
      }
    },
    
    updateScore(increment:number) {
      const storedUserInfo = localStorage.getItem('userInfo');
      if (storedUserInfo) {
        this.score = (this.score || 0) + increment;
      }
    },
    

    // 注销方法
    logout() {
      this.nickname = '';
      this.score = 1000;
      this.isAuthenticated = false;
      this.phoneNumber = '';
      this.email = '';
      this.session = '';
      this.wins = 0;
      localStorage.removeItem('userInfo');
    }
  }
});