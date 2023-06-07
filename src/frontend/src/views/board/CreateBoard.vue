<template>
  <div class="container">

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input class="form-control" type="text" v-model="title">
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" rows="10" v-model="content"></textarea>
    </div>
    <div class="mb-3">
      <label for="" class="form-label">작성자</label>
      <input type="text" class="form-control" :value="userName" readonly/>
    </div>

    <!--  <input type="file" accept="image/*" multiple/>-->
    <div>
      <button class="btn btn-light" type="button" @click="createBtn">등록</button>
    </div>
  </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import router from "@/router";

const title = ref('');
const content = ref('');

const userName = ref('');

const createBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .post("/api/community", {
        title: title.value,
        content: content.value
      }, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        if (response.data) {
          alert(response.data);
          router.replace('/community');
        }
      })
      .catch((error) => {
        if (error.response.data) {
          alert(error.response.data);
          router.push('/community/new');
        }
      });
}

// 작성자에 로그인한 회원 이름 가져오기
onMounted(() => {
  const token = Cookies.get('accessToken');

  axios
      .get("/api/community/getWriter", {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        console.log(response.data);
        userName.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});
</script>


<style scoped>
.container {
  margin-top: 100px;
  margin-bottom: 10px;
}
</style>