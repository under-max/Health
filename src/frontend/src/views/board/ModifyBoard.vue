<template>

  <div class="container-lg">

    <div class="d-flex justify-content-between">

      <div id="info">
        <span>{{ board.writer }}</span>
      </div>

      <div>
        <button type="button" class="btn btn-secondary" @click="modifyBtn">수정</button>
        <button type="button" class="btn btn-danger" @click="cancelBtn">취소</button>
      </div>

    </div>

    <div class="mb-3">
      <p class="errorMessage" v-if="errors.title">{{ errors.title }}</p>
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" v-model="board.title"/>
    </div>

    <div class="mb-3">
      <p class="errorMessage" v-if="errors.content">{{ errors.content }}</p>
      <label for="" class="form-label">내용</label>
      <textarea class="form-control" rows="10" v-model="board.content">{{board.content}}</textarea>
    </div>

  </div>

</template>

<script setup>

import {defineProps, onMounted, ref} from "vue";
import router from "@/router";
import axios from "axios";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  }
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  inserted: ''
});

const errors = ref({
  title: '',
  content: ''
});

const modifyBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .patch(`/api/community/board/${props.boardId}`, {
        title: board.value.title,
        content: board.value.content
      }, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        if (response.data) {
          showCustomAlert(response.data);
          router.replace(`/community/board/${props.boardId}`);
        }
      })
      .catch((error) => {
        errors.value = error.response.data;
      });
};

const cancelBtn = () => {
  router.replace(`/community/board/${props.boardId}`)
};

onMounted(() => {
  const token = Cookies.get('accessToken');

  axios
      .get(`/api/community/board/${props.boardId}`, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        board.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
        router.replace("/community")
      });
});

</script>

<style scoped>

.container-lg {
  margin-top: 100px;
}

#info {
  color: white;
}

.form-label {
  color: yellow;
}

.errorMessage {
  font-size: 18px;
  color: red;
}

</style>