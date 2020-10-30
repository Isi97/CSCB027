
import React, { Component } from 'react';
import { getFile } from '../util/APIUtils';
import "./imageFromBlob.css"

class ImageFromBlob extends Component {
    constructor(props) {
        super(props);

        this.state = {
            iurl: null
        }

    }

    componentDidMount() {
        var imageUrl;
        let s = this.setState.bind(this);

        getFile(this.props.imageId).then((response) => {

            var imageUrl = 'data:' + response.type + ';base64,' + response.data;

            s({
                iurl: imageUrl
            });


        })
    }

    render() {
        return (
            <img className="imageFromBlob" src={this.state.iurl === null ? "stonks" : this.state.iurl} ></img>
        );
    }
}

export default ImageFromBlob 