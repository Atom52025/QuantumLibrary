'use client';
import {Button, Image, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader,} from "@nextui-org/react";
import {useSession} from "next-auth/react";
import {useEffect, useState} from "react";
import {DELETE, PATCH} from "@/app/api/request";
import InfoPopups from "@/app/components/InfoPopups";
import TagInput from "@/app/components/TagInput";
import {GET} from "@/app/api/signalRequest";
import ImageInput from "@/app/components/ImageInput";

export default function AddGameModal({userGame, setGames, isOpen, onOpenChange }) {
    // Get Session
    const { data: session } = useSession();

    // Result modal state
    const [resultModal, setResultModal] = useState("closed");
    
    // Form data
    const [timePlayed, setTimePlayed] = useState(userGame.timePlayed);
    const [imageKey, setImageKey] = useState(0);
    const [customImage, setCustomImage] = useState("");
    const [tags, setTags] = useState(userGame.tags.split(","));
    
    // Grid Images
    const [grids, setGrids] = useState([]);

    const passImage = (increment) => {
        setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
    };

    const eraseForm = async (onClose) => {
        const formURL = `api/user/${session.user.username}/games/${userGame.game.id}`;
        console.log(formURL)

        try {
            setGames((prevGames) => {
                const newGames = [...prevGames];
                newGames.pop(userGame);
                return newGames;
            });
            await DELETE(formURL, session.user.token);
            setResultModal("successDelete");

            onClose();
        } catch (error) {
            setResultModal("error");
        }

    };

    const editForm = async (onClose) => {
        const formURL = `api/user/${session.user.username}/games/${userGame.game.id}`;

        const requestBody = {
            tags: tags.join(","),
            timePlayed: timePlayed,
            image: customImage || grids[imageKey] ,
        };

        try {
            const res = await PATCH(formURL, session.user.token, requestBody)

            setGames((prevGames) => prevGames.map((game) =>
                game.game.id === res.game.id ? res : game
            ));

            setResultModal("success");
            onClose();
        } catch (error) {
            setResultModal("error");
        }
    };

    const getGrids = async (key) => {
        console.log(userGame.game.name + " - Session: " +  session)
        const formURL = `api/sgdb/getGrids/${key}`;
        let res = await GET(formURL, session.user.token);
        let filteredGrids = res.data.filter(item => item.width === 600 && item.height === 900).map(item => item.url);
        setGrids(filteredGrids);
        return filteredGrids;
    };

    useEffect(() => {
        // Prevents from calling again if already fetched
        if (grids.length !== 0) {
            let index = grids.findIndex((grid) => grid === (userGame.image == null ? userGame.game.image : userGame.image));
            if (index !== -1) setImageKey(index);
            else setCustomImage((userGame.image == null ? userGame.game.image : userGame.image));
        }

        // Fetch grids
        getGrids(userGame.game.sgbdId).then((filteredGrids) => {
            let index = filteredGrids.findIndex((grid) => grid === (userGame.image == null ? userGame.game.image : userGame.image));
            if (index !== -1) setImageKey(index);
            else setCustomImage((userGame.image == null ? userGame.game.image : userGame.image));
        })
    }, [isOpen]);

    const renderModalContent = (onClose) => (
        <>
            <ModalHeader className="uppercase text-3xl">{userGame.game.name}</ModalHeader>
            <ModalBody className="grid grid-cols-2">
                <div className="space-y-4">
                    {/* GAME IMAGE */}
                    <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} passImage={passImage}/>
                </div>
                {/* DETAILS INPUT */}
                <div className="flex flex-col gap-4 w-full">
                    {/* TIME PLAYED */}
                    <Input
                      label="Time Played"
                      placeholder="Enter time played"
                      type="number"
                      variant="bordered"
                      onChange={(e) => setTimePlayed(e.target.value)}
                      value={timePlayed? timePlayed : 0}
                      required
                    />
                    
                    <TagInput tags={tags} setTags={setTags}/>
                </div>
            </ModalBody>
            <ModalFooter>
                <Button color="danger" onPress={() => eraseForm(onClose)}>
                    Erase
                </Button>
                <Button color="warning" onPress={() => editForm(onClose)}>
                    Edit
                </Button>
                <Button color="primary" variant="flat" onPress={onClose}>
                    Close
                </Button>
            </ModalFooter>
        </>
    );

    return (
        <>
            <Modal
                isOpen={isOpen}
                size={"3xl"}
                onOpenChange={onOpenChange}
                placement="top-center"
            >
                <ModalContent>
                    {(onClose) => renderModalContent(onClose)}
                </ModalContent>
            </Modal>
            <InfoPopups resultModal={resultModal} setResultModal={setResultModal}/>
        </>
    );
}