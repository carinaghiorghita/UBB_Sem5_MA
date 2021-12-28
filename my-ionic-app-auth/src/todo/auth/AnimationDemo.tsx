import React, { useEffect } from 'react';
import { createAnimation } from '@ionic/react';
import './Auth.css';

const AnimationDemo = (props: { text: any | undefined }) => {
    useEffect(basicAnimation, [props.text]);

    return (
        <div className="container">
            <div className="square-a">
                <p>{props.text}</p>
            </div>
        </div>);

    function basicAnimation() {
        const el = document.querySelector('.square-a');
        if (el) {
            const animation = createAnimation()
                .addElement(el)
                .duration(1000)
                .direction('alternate')
                .iterations(Infinity)
                .keyframes([
                    {offset: 0, transform: 'scale(1.5)', opacity: '1'},
                    {
                        offset: 1, transform: 'scale(1)', opacity: '1'
                    }
                ])
                .beforeStyles({
                    'color': 'red'
                });
            animation.play();
        }
    }
}


export default AnimationDemo;