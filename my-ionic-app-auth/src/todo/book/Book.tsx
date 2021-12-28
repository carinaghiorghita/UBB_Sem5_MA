import {createAnimation, IonCard, IonCardHeader, IonCardSubtitle, IonCardTitle, IonModal} from "@ionic/react";
import React, {useEffect, useState} from "react";
import {BookProps} from "./BookProps";

interface BookPropsExtended extends BookProps {
    onEdit: (_id? : string) => void;
}

const Book: React.FC<BookPropsExtended> = ({_id, title, author, publishingDate, owned, latitude, longitude, webViewPath, onEdit}) => {

    useEffect(() => {
        if(document.getElementById("item") !== null) {
            // @ts-ignore
            document.getElementById("item")!.addEventListener('click', () => {
                onEdit(_id);
            });
        }
    }, [document.getElementById("item")]);


    return (
        <IonCard onClick={ () => onEdit(_id) }>
            <IonCardHeader>
                <IonCardTitle>{title}</IonCardTitle>
                <IonCardSubtitle>{author}</IonCardSubtitle>
                <IonCardSubtitle>{publishingDate}</IonCardSubtitle>
                <IonCardSubtitle>You { owned ? '' : 'do not' } own this book.</IonCardSubtitle>
                <IonCardSubtitle>Latitude: {latitude}</IonCardSubtitle>
                <IonCardSubtitle>Longitude: {longitude}</IonCardSubtitle>
                {webViewPath && (<img id="image" src={webViewPath} width={'160px'} height={'250px'} />)}
                {!webViewPath && (<img src={'https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg'} width={'200px'} height={'200px'} />)}
            </IonCardHeader>
        </IonCard>
    )
};


export default Book;