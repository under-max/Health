<template>
    <main class="hero">
        <section class="main_text">
            <h1>당신의 노력을 서포트 <br>합니다</h1>
            <p>특별한 당신을 위해서 전문적 지식을 갖춘 트레이너를 통해<br>
                당신만을 위한 차별화된 맞춤형 운동, 식단 그날그날의<br>
                운동정보를 실시간으로 확인하세요</p>
                <button>문의하기</button>
        </section>
        <section class="main_image">
            <img :src="Main_Image2" alt="메인이미지">
        </section>        
        <p>main 2번째 페이지랑 footer 부분은 반응형 정도는 나중에 기능 개발 하면
        <br> 중점되는 부분 2번쨰 페이지 혹은 공지사항 그냥 2번쨰에 넣고
        footer는 그냥 주저리 부분이라 시간날떄 채우도록 하겠음
        </p>
    </main>
</template>

<script setup lang="js">
import Main_Image from '@/assets/Hero_Image.png';
import Main_Image2 from '@/assets/M_Image.png';
</script>

<style scoped>
.hero{
    margin: 0px;
    width: auto;
    height: auto;
    min-height: 100vh;
    background-color: #112470;
    background-image: linear-gradient(176deg, #112470 35%, #fff);
    flex-wrap: wrap;
    justify-content: space-between;
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

.main_text > h1{
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

.main_image{
    max-width: 100%;
    margin-left: 0px;
    margin-right: 0px;
}

.main_image > img {    
    height: 100vh;    
}

</style>