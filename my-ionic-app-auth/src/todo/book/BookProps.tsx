
export interface BookProps {
    _id?: string;
    title: string;
    author: string;
    publishingDate: string;
    owned: boolean;
    latitude?: number;
    longitude?: number;
    webViewPath: string;
}