<template>
    <div v-if="checkModal">
        <h1>모달창 열리는지 확인중</h1>
        <button @click="closeModal">창닫기</button>
    </div>
</template>

<script setup>
import { ref, defineEmits, defineProps } from 'vue';

const props = defineProps({
    checkModal: Boolean,
})

const emit = defineEmits(["closeModal"]);

const closeModal = () => {
    emit("closeModal");
}
</script>

<style>

</style>