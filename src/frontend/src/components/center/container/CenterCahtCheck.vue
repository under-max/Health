<template>
    <div class="modal">
      <div class="modal-content chat-wrap">
        <div class="chatt">
            <p class="sendZoneName">이용자:{{user.name}}</p>
            <!-- <input disabled="disabled" type="text" id="name" v-model="user.name" class="sendZoneName"/> -->
          <div>
            <!-- <h1 class="title">WebSocket Chatting</h1>             -->
          </div>
          <br/>
          <div class="talk">
            <div class="talk-shadow"></div>
            <div v-if="chatt !== null">
              <div v-for="(message, index) in chatt" :key="index" :class="message !== null ? message.className : ''">
                <p class="name">{{ message && message.name }}</p>
                <p class="message">{{ message && message.msg }}</p>
                <p class="date">{{ message && message.date }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <div class="sendZone">
            <textarea class="msg" placeholder="메세지를 입력하세요." v-model="msg"></textarea>
            <input type="button" value="전송" class="btnSend" @click="send" />
        </div>
        <button class="closeBtn" @click="closeModal">닫기</button>
      </div>
    </div>
  </template>


<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';

//modal 처리
const props = defineProps({
    centerChat: Boolean,
    user: Object,
    id:String,
});

const emit = defineEmits(["centerChatBtn"]);

const closeModal = () => {
  emit("centerChatBtn");
  
};

const userCheck = props.user;
const centerIdCheck = +(props.id);
//db용 데이터 처리

const msg = ref('');
const name = ref('');
const chatt = ref([]);
const chkLog = ref(false);
const socketData = ref(null);
const ws = ref(null);

onMounted(() => {
  if (socketData.value !== undefined) {
    const tempData = chatt.value.concat(socketData.value);
    chatt.value = tempData;
  }
});

const webSocketLogin = () => {
  ws.value = new WebSocket('ws://localhost:8090/center/receive');

  ws.value.onmessage = (message) => {
    const dataSet = JSON.parse(message.data);
    socketData.value = dataSet;
    // chatt 배열에 추가
    chatt.value.push(...dataSet);
  };
};

const send = () => {
  name.value = userCheck.name;
  if (!chkLog.value) {
    if (name.value === '') {
      alert('이름을 입력하세요.');
      document.getElementById('name').focus();
      return;
    }
    webSocketLogin();
    chkLog.value = true;
  }

  if (msg.value !== '') {
    const data = {
      name: name.value,
      msg: msg.value,
      date: new Date().toLocaleString(),
      memberId: userCheck.id,
      centerId: centerIdCheck,
      email: userCheck.email,
      authority: userCheck.authority,
    };
    console.log(data);
    const temp = JSON.stringify(data);

    if (ws.value.readyState === 0) {
      ws.value.onopen = () => {
        ws.value.send(temp);
      };
    } else {
      ws.value.send(temp);
    }

    msg.value = '';
  } else {
    alert('메세지를 입력하세요.');
    document.getElementById('msg').focus();
    return;
  }
};

onUnmounted(() => {
  if (ws.value) {
    ws.value.close();
    console.log('WebSocket connection closed');
  }
});
</script>

<style scoped>
 .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.2);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .modal-content {
    background-color: #9bbbd4;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
    width: 40vw;
    height: 80vh;
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
  }

  .chatt {
    flex: 1;
    overflow-y: auto;
  }

  .title {
    font-size: 30pt;
    text-align: center;
    margin-bottom: 20px;
  }

  .talk {    
    flex: 1;
    border-radius: 18px;
    position: relative;
  }

  .talk-shadow {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }





  .talk div {
    min-width: 60%;
    width: 70%;
    max-width: 80%;
  display: block;
  padding: 10px;
  border-radius: 10px;
  box-sizing: border-box;
  margin-left: auto;
  text-align: right;
}

  .talk div.me {
    background-color: #ffc;
    text-align: right;   
}

.talk div.other {
    margin: 0;
    text-align: left; 
  }





  
  #name {
    display: block;
    border: 1px solid #dcdcdc;
    background-color: #ededed;
    padding: 5px 2px;
    margin-top: 20px;
  }

  .sendZoneName {
  width:30%;
  border: none;
  text-align: center;
}

.sendZone {
  display: flex;
  align-items: center;
  margin-bottom: 1vh;
}

.msg {
  flex: 1;
  height: 70px;
  resize: none;
  border: 1px solid #dcdcdc;
  background-color: #fff;
  border-radius: 20px 0 0 20px; /* 왼쪽은 둥근 테두리, 오른쪽은 직각 테두리 */
  padding: 10px;
}

.btnSend {
  width: 100px;
  height: 70px;
  background: #fef01b;
  color: black;
  border: 1px solid #dcdcdc;
  border-radius: 0 20px 20px 0; /* 왼쪽은 직각 테두리, 오른쪽은 둥근 테두리 */
}



  .closeBtn {
    margin-top: auto;
    border-bottom: none;
    margin-bottom: 0;
    padding: 8px 16px;
    background-color: #2196f3;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    border-radius: 20px;
  }

  .name{
    margin-bottom: 0;
  }

  .message{
    margin-bottom: 0;
  }

  .date{
    margin-bottom: 0;
  }
</style>