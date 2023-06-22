<template>
  <div class="container-lg">

    <div class="mb-3">
      <p class="errorMessage" v-if="errors.title">{{ errors.title }}</p>
      <label for="" class="form-label">제목</label>
      <input class="form-control" type="text" placeholder="제목을 입력해주세요." v-model="title">
    </div>

    <div class="mb-3">
      <p class="errorMessage" v-if="errors.content">{{ errors.content }}</p>
      <label for="" class="form-label">내용</label>
      <textarea class="form-control" rows="10" placeholder="내용을 입력해주세요." v-model="content"></textarea>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">작성자</label>
      <input type="text" class="form-control" :value="userName" readonly/>
    </div>

    <div class="d-flex justify-content-end">
      <button class="btn btn-primary" type="button" @click="createBtn">등록</button>
      <button class="btn btn-danger" type="button" @click="goListBtn">취소</button>
    </div>

  </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import router from "@/router";

const errors = ref({
  title: '',
  content: ''
});

const title = ref('');
const content = ref('');

const userName = ref('');

const goListBtn = () => {
  router.push('/community')
}

const createBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .post("/api/community/board", {
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
        errors.value = error.response.data;
      });
}

// 작성자에 로그인한 회원 이름 가져오기
onMounted(() => {
  const token = Cookies.get('accessToken');

  axios
      .get("/api/community/board/getWriter", {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        userName.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
});

</script>

<style scoped>
.container-lg {
  margin-top: 100px;
}

.form-label {
  color: yellow;
}

.errorMessage {
  font-size: 18px;
  color: red;
}

</style>