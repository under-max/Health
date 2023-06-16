<template>
  <hr>
  <hr>
  <div class="container row justify-content-left">
    <div class="row">
      <div class="col">
        <h1 class="text-center">{{ user.name }}님의 프로필</h1>
        <div class="mb-3">
          <div class="mb-3">
            <label for="" class="form-label">이름</label>
            <input type="text" class="form-control" :value="user.name" id="inputUserName" readonly>
          </div>
          <div class="mb-3">
            <label for="" class="form-label">센터 이름</label>
            <input type="text" class="form-control" :value="user.centerName" id="inputCenterName" readonly>
          </div>
          <div class="mb-3">
            <label for="" class="form-label">센터 주소</label>
            <input type="text" class="form-control" :value="user.centerAddress" id="inputCenterAddress" readonly>
          </div>
          <div class="mb-3">
            <label for="" class="form-label">담당 트레이너</label>
            <input type="text" class="form-control" :value="user.trainerName" id="inputTrainerName" readonly>
          </div>
          <div class="mb-3">
            <label for="" class="form-label">이용권 기간</label>
            <input type="text" class="form-control" :value="periodMembership" id="inputPeriodMembership" readonly>
          </div>
          <div class="mb-3">
            <label for="" class="form-label">남은 PT 횟수</label>
            <input type="text" class="form-control" :value="user.remainingPT" id="inputRemainingPT" readonly>
          </div>

        </div>
      </div>
      <div class="col">
        <h2 class="text-center">담당 트레이너 {{ user.trainerName }} 채팅</h2>
        <h2 class="text-center" style="background-color: white">채팅창</h2>
        <div class="container messageBox" style="background-color: white">
          <div>
            <div>
              <div v-for="chat in messageList" :key="chat.chatId">
                <div>
                  <strong>{{ chat.memberName }}</strong> ({{ chat.timestamp }})
                </div>
                <div>{{ chat.message }}</div>
              </div>
            </div>
          </div>
        </div>
        <input class="messageInput" v-model="newMessage" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요">
        <button @click="sendMessage"> 전송</button>

      </div>
      <!--      <div class="col">-->
      <!--        <h1 class="text-center">트레이너 채팅</h1>-->

      <!--      </div>-->
    </div>
  </div>

</template>

<script setup>
import {defineProps, nextTick, onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";

const newMessage = ref('');
const messageList = ref({
  chatId: '',
  memberName: '',
  timestamp: '',
  message: ''
});
const chat = ref({
  messageFrom: '',
  messageTo: '',
  timestamp: '',
  message: ''
});

const sendMessage = () => {
  if (newMessage.value) {
    const token = Cookies.get("accessToken");
    axios.post("/api/chat/saveMessage", {
      messageFrom: user.value.id,
      messageTo: user.value.trainerId,
      message: newMessage.value
    }, {
      headers : {
        Authorization : token
      }
    })
        .then((response) => {
          // chats.value.push(chat);
          console.log("messageFrom", user.value.id)
          console.log("messageTo", user.value.trainerId)
          console.log("message", newMessage.value)
          newMessage.value = '';
          location.reload();
        })
        .catch((error) => {
          alert((error))
        })

  }
}


const props = defineProps({
  userId: {
    type: [Number, String],
    required: true
  },
});

const periodMembership = ref();


const user = ref({
  id: '',
  name: '',
  email: '',
  isInCenter: '',
  trainerId: '',
  trainerName: '',
  centerName: '',
  startDate: '',
  endDate: '',
  remainingPT: '',
  authority: ''

});

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.get("/api/chat/getMessageList", {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log(response.data)
        messageList.value = response.data;
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      })
})


onMounted(() => {
  const token = Cookies.get("accessToken")
  console.log(props.userId)
  axios.get(`/api/user/get/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log(response.data)
        user.value = response.data;
        periodMembership.value = `${user.value.startDate} ~ ${user.value.endDate}`

      }).catch((error) => {
    if (error.response) {
      alert(error.response.data.message);
    }
  });
});

</script>


<style scoped>
h1 {
  color: black;
}

label {
  color: white;
}

.container {
  max-width: 900px;
  height: 450px;
}

.messageBox {
  overflow-y: auto;
}

.messageInput {
  width: 355px;
}

</style>
