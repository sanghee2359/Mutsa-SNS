// 헤더 부분에서 네비게이션 역할을 해 줄 아이콘을 위한 컴포넌트
import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

const Icon = styled.img`
    width: 40px;
    height: 40px;
    margin-left: 20px;
    margin-right: 20px;
`;

const Shortcut = ({ path, src }) => {
    return (
        <Link to={ path } >
            <Icon src={ src } />
        </Link>
    );
}

export default Shortcut;