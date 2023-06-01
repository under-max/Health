<template>
  <div class="black-bg">
    <div class="white-bg">

      <div class="result_centerName_bg" v-if="resultModal">
        <div class="result_centerName">
          <h4 v-if="centerLength !== 0">{{centerLength}}개의 결과를 찾았습니다.</h4>
          <h4 v-if="centerLength === 0">검색결과가 없습니다 다시 입력해 주세요</h4>
          <h5 v-if="centerLength !== 0">찾으시는 값을 선택해 주세요</h5>
          
          
            <div v-for="(data, index) in tr_Result" :key="index">              
                <label :for="'center'+index">
                  {{searchChecker === 1 ? data.name : ""}}
                  {{searchChecker === 2 ? "이름: " + data.name +"  /////  "+ "e-mail: "+data.email : ""}}
                </label>
                <input :id="'center'+index" 
                type="radio" 
                :value="searchChecker === 1 ? data.id : searchChecker === 2 ? data.id : ''" 
                v-model="idChecker" 
                name="centerNameOption" 
                >
                <br>                            
            </div>
          

          <button @click="resultModal = false">닫기</button>
        </div>
      </div>
      

      <h3>트레이너 정보 등록</h3>
      <!-- 들어갈게 등록하면 authority 값2 
                trainerFile 에는 trainerId, centerId, fileName 
                Trainer 에는 memberId, centerId, info
                Member에서는 (name, e-mail)=> 트레이너 이름 동명이인 구분용, authority
            -->
      <hr />
      <form enctype="multipart/form-data">
        <!-- 해당하는 센터 검색 -->
        <label for="center">센터검색: </label>
        <input id="center" v-model="centerName" />
        <button
          @click="tr_centerSearch_btn"
          @keydown.enter="tr_centerSearch_btn"
        >
          검색
        </button>
        <hr>
        <!-- Member 테이블에서 get요청 이름으로 검색, 표시값 이름 이메일 -->
        <label for="searchMemberForTrainer">회원검색: </label>
        <input type="text" id="searchMemberForTrainer" @keydown.enter="tr_memberSearch_btn" v-model="memberName">
        <button @click="tr_memberSearch_btn">검색</button>
        <hr>
        <!-- 결과표시 -->
        <div v-if="showData.center !== '' && showData.memberName !== ''">
          <h3>센터명: {{showData.center}}</h3>
          <h3>이름  : {{showData.memberName}}</h3>
          <h3>별명  : {{showData.nickName}}</h3>
          <h3>이메일: {{showData.email}}</h3>
          <br>   
          <label for="trainer_input_info">트레이너 정보를 입력하세요</label><br>
          <textarea id="trainer_input_info" v-model="trainerData.info"/><br>   
          
          <label for="trainer_pic">트레이너 사진</label><br>
          <input type="file" 
          id="trainer_pic"
          accept="image/*"
          @change="trainer_img"
          >          
          <br><hr>
          <button type="submit" @click="trainerInfoSubmitBtn">등록하기</button>
        </div>

      </form>
      <hr>
      <button @click="closeModal">닫기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from "vue";
import axios from "axios";


//url
const url = "http://localhost:8090/";

//Modal props 설정
const props = defineProps({
  trainerRegisterModal: Boolean,
});

const emit = defineEmits(["closeTrainerRegisterModal"]);

const closeModal = () => {
  emit("closeTrainerRegisterModal");
};
// 전체 값 모아서 화면 보여줄 변수 선언
const showData = ref({
  center: '',
  memberName: '',
  nickName: '',
  email: '',  
})

// 찾아온 값 저장 추후 controller로 Data 보내는 변수 선언
const trainerData = ref({
  memberId: 0,
  centerId: 0,
  info: '',
  name: '',
  nickName: '',
  authority: 2
})

//검색 구분자
const searchChecker = ref(0);

//등록시 id 구분용 checker 이후 값 넘겨줌 Member는 값 뽑아서 data변수에 저장
const idChecker = ref(0);

watch(idChecker,(curValue, oldValue)=>{  
  if(searchChecker.value === 1){
    trainerData.value.centerId = idChecker.value;    
    tr_Result.value.map(data=>{
      showData.value.center = data.name;
    })
    console.log(showData.value);
  }else if(searchChecker.value === 2){
    trainerData.value.memberId = idChecker.value;
    tr_Result.value.map(data => {         
      if(data.id === trainerData.value.memberId){
        trainerData.value.nickName = data.nickName;
        trainerData.value.name = data.name;
        console.log(trainerData.value);
        showData.value.memberName = data.name;
        showData.value.email = data.email;
        showData.value.nickName = data.nickName;
      }
    })
    console.log(showData.value);
  }
});


//CenterName Data 가져오기
const centerName = ref("");
const tr_Result = ref([]);
const tr_centerSearch_btn = async (e) => {
  e.preventDefault();
  console.log(centerName.value);

  try {
    const response = await axios.get(url + "center/centerNameInfo", {
      params: {
        centerName: centerName.value,
      },
    });    
    tr_Result.value = response.data;
    searchChecker.value = 1;
    resultModal.value = true;
  } catch (error) {
    console.log(error);
  }
};


//CenterName 가져온 후 Result Modal 창
const resultModal = ref(false);
const centerLength = ref(0);

watch(tr_Result,(currValue, oldValue)=>{  
  centerLength.value = currValue.length
});


// 찾아온 CenterName 클릭 후 TrainerData CenterId에 변수 저장 처리 완료

//Member검색해서 조회 후 선택 및 memberId 에 저장
const memberName = ref('');
const tr_memberSearch_btn = async(e) => {
  e.preventDefault();

  //값 받아오기 위한 초기화
  tr_Result.value = []; 
  
  try{
    const response = await axios.get(url+"center/membersearch",{
      params:{
        memberName: memberName.value
      }
    });
    console.log("성공");
    tr_Result.value = response.data;
    console.log(tr_Result.value);
    searchChecker.value = 2;
    resultModal.value = true;
  }catch(error){
    console.log("실패");
  }
}

//트레이너 정보 create 로직

//파일 체크 
const trainer_pic = ref([]);

const trainer_img = (e) => {
  const files = e.target.files;
  // trainer_pic.value.push(files);
  for (let i = 0; i < files.length; i++) {
    trainer_pic.value.push(files[i]);
  }
};

// Controller 송신
const trainerInfoSubmitBtn = async(e) => {
  e.preventDefault();
  console.log(trainer_pic.value[0]);
  console.log(trainerData.value);

  const trainerTotalData = new FormData();
  for (let i = 0; i < trainer_pic.value.length; i++) {
    trainerTotalData.append("trainerImg", trainer_pic.value[i]);
  }
  

  trainerTotalData.append("memberId", trainerData.value.memberId);
  trainerTotalData.append("centerId", trainerData.value.centerId);
  trainerTotalData.append("info", trainerData.value.info);
  trainerTotalData.append("name", trainerData.value.name);
  trainerTotalData.append("nickName", trainerData.value.nickName);
  trainerTotalData.append("authority", trainerData.value.authority);


  try{
    await axios.post(url+"center/inputInfoTrainer",trainerTotalData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }); 
    console.log("성공");   
  }catch(error){
    console.log(error);
  }
}

</script>

<style scoped>
h1 {
  color: black;
}



.result_centerName_bg{
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 998;
  background-color: rgba(0, 0, 0, 0.4);
}
.result_centerName {
  z-index: 999;  
  position: absolute;
  width: 50%;
  height: 50%;
  top: 50%; 
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 30px;
}


.black-bg {
  width: 90%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 30px;
  padding: 20px;
}
</style>