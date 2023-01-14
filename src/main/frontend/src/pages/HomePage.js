import React from 'react';
import HeaderTemplate from '../components/common/HeaderTemplate';

const HomePage = () => {
    return(
        <>
            <HeaderTemplate />
            <h1>매일이 특별한 하루</h1>
            <p>세 줄 일기로 당신의 하루를 적어보세요</p>
            <p>당신의 속마음을 사람들에게 공유해보세요</p>
        </>
    );
};

export default HomePage;