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
      <div class="error" v-if="errors.title">
        <p>{{ errors.title }}</p>
      </div>
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" v-model="board.title"/>
    </div>

    <div class="mb-3">
      <div class="error" v-if="errors.content">
        <p>{{ errors.content }}</p>
      </div>
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" rows="10" v-model="board.content">{{board.content}}</textarea>
    </div>

    <div class="d-flex justify-content-end">

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
        console.log(error);
        if (error.response.data) {
          alert(error.response.data.message);
        } else {
          alert(error.response.data);
        }
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

.error {
  font-size: 20px;
  color: red;
}

</style>