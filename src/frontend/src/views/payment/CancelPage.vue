<template>

  <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">

        <div class="modal-body">
          <h5>결제가 취소되었습니다.</h5>
        </div>

        <div class="modal-footer">
          <button class="btn btn-primary" @click="goMembershipBtn">이용권 안내 화면으로</button>
          <button class="btn btn-secondary" @click="goHomeBtn">홈 화면으로</button>
        </div>

      </div>
    </div>
  </div>

</template>

<script setup>
import {ref, onMounted} from 'vue';
import router from "@/router";

const showModal = ref(false);

const goHomeBtn = () => {
  router.replace('/');
}

const goMembershipBtn = () => {
  router.replace('/membership');
}

onMounted(() => {
  showModal.value = true;
});

</script>

<style scoped>

.modal {
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  padding: 5px;
}

</style>
