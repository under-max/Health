<template>

  <div class="main_popup" v-bind:class="{ on: showPopup }">
    <div class="layer_cont">
      <div class="img_wrap">
        popup content
      </div>
      <div class="btn_wrap">
        <!-- Don't watch today --->
        <button class="btn_today_close" @click="hidePopupToday"><span>Do not close today</span></button>
        <!-- just close --->
        <button class="btn_close" @click="hidePopup">close</button>
      </div>
    </div>
  </div>

  <div v-if="isRemainingPT()">
    <div class="position-fixed top-0 end-0 p-3">
      <hr>
      <div class="toast show" style="width: 180px" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="1000">
          <div class="toast-header">
            <strong class="me-auto text-danger">알림</strong>
            <small></small>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div class="toast-body">
            PT {{ remainingPT }}회 남았습니다.
          </div>
        </div>
      </div>
  </div>

  <main class="hero">
    <section class="main_text">
      <h1>당신의 노력을 서포트 <br>합니다</h1>
      <p>특별한 당신을 위해서 전문적 지식을 갖춘 트레이너를 통해<br>
        당신만을 위한 차별화된 맞춤형 운동, 식단 그날그날의<br>
        운동정보를 실시간으로 확인하세요</p>
      <button @click="goToSearchCenter">지점찾기</button>
    </section>
    <section class="main_image">
      <img :src="Main_Image2" alt="메인이미지">
    </section>
  </main>
  <footer class="footerContainer">
    <div class="footerDiv">
      <p>(주)건강한사람들 | 대표자: 허재엽 | 사업자번호: 111-11-11111<br/> 
        통신판매업: 2018-성남분당B-0062 | 개인정보보호책임자: Adam | 이메일: aaaa@naver.com<br/> 
        주소: 경기도 성남시 분당구 대왕판교로 660 유스페이스 1A동 405호<br/>
        전화번호: 02-1111-111<br/> 
        ©Healter. ALL RIGHTS RESERVED<br/> 
      </p>
    </div>
  </footer>

</template>

<script setup lang="js">
import Main_Image2 from '@/assets/M_Image.png';
import Cookies from "vue-cookies";
import {onMounted, ref} from "vue";
import axios from "axios";
import { useRouter } from 'vue-router';
const showPopup = ref(false);

const router = useRouter();

const goToSearchCenter = () => {
  router.push('/center');
}

// Define storage control functions
const handleStorage = {
  // write data to storage (name, expiration date)
  setStorage: function (name, exp) {
    // Get the expiration time (change exp to ms)
    var date = new Date();
    date = date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);

    // save to local storage
    // (store the expiration time without storing the value separately)
    localStorage.setItem(name, date);
  },
  // read storage
  getStorage: function (name) {
    var now = new Date();
    now = now.setTime(now.getTime());
    // Compare the current time with the time stored in the storage
    // returns true if time is left, false otherwise
    return parseInt(localStorage.getItem(name)) > now;
  },
};

// Read cookie and show popup
const toggleMainPopup = () => {
  if (handleStorage.getStorage('today')) {
    showPopup.value = false;
  } else {
    showPopup.value = true;
  }
};

// Hide popup for today
const hidePopupToday = () => {
  handleStorage.setStorage('today', 1);
  showPopup.value = false;
};

// Hide popup
const hidePopup = () => {
  showPopup.value = false;
};

onMounted(() => {
  toggleMainPopup();
});

const remainingPT = ref();
const isRemainingPT = () => {
  return (Cookies.get('accessToken') && (remainingPT.value >= 0 && remainingPT.value <= 5));
}

onMounted(() => {
  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .get("/api/membership/remainingPT", {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          remainingPT.value = response.data;
        })
        .catch((error) => {
          console.log(error)
        });
  }
});
</script>

<style scoped>
.hero {
  margin: 0px;
  width: auto;
  height: auto;
  min-height: 100vh;
  background-color: #112470;
  background-image: linear-gradient(176deg, #112470 35%, #fff);
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;
  padding: 0 0 0 60px;
  display: flex;
}

.main_text {
  max-width: 1160px;
  min-width: 400px;
  margin-left: 10vh;
  margin-right: 10vh;
  padding-top: 28vh;
  padding-bottom: 28vh;
}

.main_text > h1 {
  color: white;
  margin-top: 0px;
  margin-bottom: 28px;
  padding-top: 50px;
  font-size: 65px;
  line-height: 70px;
}

.main_text > p {
  color: white;
  margin-bottom: 25px;
  font-size: 23px;
  line-height: 40px;
}


.main_text > button {
  height: 65px;
  text-align: center;
  border-radius: 50px;
  margin-left: 0px;
  padding: 15px 61px;
  font-size: 23px;
  color: white;
  background: rgba(255, 130, 0, 0.9);
  border: none;
}

.main_image {
  max-width: 100%;
  margin-left: 0px;
  margin-right: 0px;
}

.main_image > img {  
  height: 100vh;
}

button {
  border: 0;
  background: none;
  cursor: pointer;
}

.main_popup {
  position: fixed;
  z-index: 1005;
  -webkit-box-shadow: 0px 13px 40px -6px #061626;
  box-shadow: 0px 13px 40px -6px #061626;
  top: 50px;
  left: 50px;
  display: none;
}
.main_popup.on {
  display: block;
  background-color: #fff;
}
.main_popup .img_wrap {
  width: 200px;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.main_popup .btn_close {
  width: 32px;
  height: 32px;
  position: absolute;
  top: 17px;
  right: 17px;
  font-size: 0;
  border: 0;
  background: none;
}
.main_popup .btn_close::before {
  content: "";
  width: 2px;
  height: 32px;
  background-color: #333;
  position: absolute;
  top: 0;
  left: 15px;
  transform: rotate(45deg);
}
.main_popup .btn_close::after {
  content: "";
  width: 32px;
  height: 2px;
  background-color: #333;
  position: absolute;
  top: 15px;
  left: 0;
  transform: rotate(45deg);
}
.main_popup .btn_today_close {
  width: 100%;
  height: 45px;
  background-color: #333;
  text-align: center;
  color: #fff;
  font-size: 14px;
  display: block;
}
.main_popup .btn_today_close span {
  display: block;
  line-height: 40px;
  vertical-align: bottom;
  opacity: 0.8;
}

.footerContainer{
  background: rgb(48, 55, 64);
}

.footerDiv{
  padding-top: 3vh;
  padding-bottom: 3vh;
  margin-left: 200px;
  margin-right: 200px;
}
.footerDiv p{
  color: rgb(180, 180, 180);
}

</style>