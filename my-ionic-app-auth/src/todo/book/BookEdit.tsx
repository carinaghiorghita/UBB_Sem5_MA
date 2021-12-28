import {
    createAnimation,
    CreateAnimation,
    IonButton,
    IonButtons,
    IonContent,
    IonDatetime,
    IonHeader,
    IonInput,
    IonItem,
    IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToggle,
    IonToolbar
} from '@ionic/react';
import React, { useEffect } from 'react';
import { useContext, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import { BookContext } from './BookProvider';
import { BookProps } from './BookProps';
import { MyMap } from "../../core/MyMap";
import { UseMyLocation } from "../../core/UseMyLocation";
import { UsePhotoGallery } from "../../core/UsePhotoGallery";
import moment from 'moment';


interface BookEditProps extends RouteComponentProps<{
    id?: string;
}> {}

const BookEdit: React.FC<BookEditProps> = ({history, match}) => {
    const {items, saving, savingError, saveItem } = useContext(BookContext);
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [publishingDate, setPublishingDate] = useState('');
    const [owned, setOwned] = useState(false);
    const [item, setItem] = useState<BookProps>();

    const [latitude, setLatitude] = useState<number | undefined>(undefined);
    const [longitude, setLongitude] = useState<number | undefined>(undefined);
    const [currentLatitude, setCurrentLatitude] = useState<number | undefined>(undefined);
    const [currentLongitude, setCurrentLongitude] = useState<number | undefined>(undefined);
    const [webViewPath, setWebViewPath] = useState('');
    const location = UseMyLocation();
    const {latitude : lat, longitude : lng} = location.position?.coords || {};
    const {takePhoto} = UsePhotoGallery();

    useEffect(() => {
        log('useEffect');
        const routeId = match.params.id || '';
        const item = items?.find(it => it._id === routeId);
        setItem(item);
        if (item) {
            setTitle(item.title);
            setAuthor(item.author);
            setPublishingDate(item.publishingDate);
            setOwned(item.owned);
            setLatitude(item.latitude);
            setLongitude(item.longitude);
            setWebViewPath(item.webViewPath);
        }
    }, [match.params.id, items]);

    useEffect(() => {
        if (latitude === undefined && longitude === undefined) {
            setCurrentLatitude(lat);
            setCurrentLongitude(lng);
        } else {
            setCurrentLatitude(latitude);
            setCurrentLongitude(longitude);
        }
    }, [lat, lng, longitude, latitude]);

    const handleSave = () => {
        log('entered handleSave');
        const editedItem = item ? {...item, title, author, publishingDate, owned, latitude, longitude, webViewPath } : { title, author, publishingDate, owned, latitude, longitude, webViewPath };
        console.log(editedItem);
        saveItem && saveItem(editedItem).then(() => {history.goBack()});
    };

    async function handlePhotoChange() {
        const image = await takePhoto();
        if (!image) {
            setWebViewPath('');
        } else {
            setWebViewPath(image);
        }
    }

    function setLocation() {
        setLatitude(currentLatitude);
        setLongitude(currentLongitude);
    }

    // animations

    const cameraElement = document.querySelector('.camera');
    const positionElement = document.querySelector('.position');
    if (cameraElement && positionElement) {
        const cameraFabAnimation = createAnimation()
            .addElement(cameraElement)
            .duration(2000)
            .fromTo('transform', 'translateX(300px)', 'translateX(0px)');
        const positionFabAnimation = createAnimation()
            .addElement(positionElement)
            .duration(2000)
            .fromTo('transform', 'translateX(-300px)', 'translateX(0px)');
        (async () => {
            await cameraFabAnimation.play();
            await positionFabAnimation.play();
        })();
    }

     return (
     <IonPage>
        <IonHeader>
            <IonToolbar>
                <IonTitle>Edit book</IonTitle>
                <IonButtons slot="end">
                    <CreateAnimation
                        duration={2000}
                        iterations={Infinity}
                        keyframes={
                            [
                                { offset: 0, transform: 'scale(1)' },
                                { offset: 0.5, transform: 'scale(1.5)' },
                                { offset: 1, transform: 'scale(1)' }
                            ]
                        }
                    >
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </CreateAnimation>                </IonButtons>
            </IonToolbar>
        </IonHeader>
        <IonContent>
            <IonItem>
                <IonLabel>Title:  </IonLabel>
                <IonInput placeholder="title" value={title} onIonChange={e => setTitle(e.detail.value || '')}/>
            </IonItem>
            <IonItem>
                <IonLabel>Author:  </IonLabel>
                <IonInput placeholder="author" value={author} onIonChange={e => setAuthor(e.detail.value || '')}/>
            </IonItem>
            <IonItem>
                <IonLabel>Publishing Date: </IonLabel>
                <IonDatetime displayFormat="DD MMM YYYY" pickerFormat="DD MMM YYYY" value={publishingDate} onBlur={e => setPublishingDate((moment(e.target.value).format('DD MMM YYYY')) || moment(new Date()).format('DD MMM YYYY'))}/>
            </IonItem>
            <IonItem>
                <IonLabel>Do you own this book? </IonLabel>
                <IonToggle checked={owned} onIonChange={e => setOwned(e.detail.checked)}/>
            </IonItem>
            <IonItem>
                <IonLabel>Where was the book published?</IonLabel>
                <IonButton onClick={setLocation}>Set location</IonButton>
            </IonItem>
            <div className={'camera'}>
            {webViewPath && (<img onClick={handlePhotoChange} src={webViewPath} width={'160px'} height={'256px'}/>)}
            {!webViewPath && (<img onClick={handlePhotoChange} src={'https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg'} width={'100px'} height={'100px'}/>)}
            </div>
            <div className={'position'}>
            {lat && lng &&
            <MyMap
                lat={currentLatitude}
                lng={currentLongitude}
                onMapClick={log('onMap')}
                onMarkerClick={log('onMarker')}
                />
            }
            </div>
            <IonLoading isOpen={saving}/>
            {savingError && (
                <div>{savingError?.message || 'Failed to save book'}</div>
            )}
        </IonContent>
     </IonPage>
    );

    function log(source: string) {
        return (e: any) => {
            setCurrentLatitude(e.latLng.lat());
            setCurrentLongitude(e.latLng.lng());
            console.log(source, e.latLng.lat(), e.latLng.lng());
        }
    }

};


export default BookEdit;