'use client';
import {
    Button,
    Card,
    CardBody,
    Chip,
    Image,
    Input,
    Modal,
    ModalBody,
    ModalHeader,
    ModalContent,
    ModalFooter,
    useDisclosure,
} from "@nextui-org/react";
import {useSession} from "next-auth/react";
import SearchBar from "@/app/components/SearchBar";
import {useEffect, useState} from "react";
import {POST} from "@/app/api/postRequest";

export default function AddGameModal({setGames}) {
    // Get Session
    const { data: session } = useSession();
    // Modal state
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    // Result modal state
    const [resultModal, setResultModal] = useState("closed");
    // Form data
    const [newGame, setNewGame] = useState(0);
    const [timePlayed, setTimePlayed] = useState();
    const [imageKey, setImageKey] = useState(0);
    const [customImage, setCustomImage] = useState("");
    const [tags, setTags] = useState([]);
    // Tag input
    const [tagInput, setTagInput] = useState("");
    // Grid Images
    const [grids, setGrids] = useState([
        "https://cdn2.steamgriddb.com/grid/41a69c66f821f25c8184aea3bb35225d.png",
        "https://cdn2.steamgriddb.com/grid/630cd7fdd075a12a67247fbce368b422.jpg",
        "https://cdn2.steamgriddb.com/grid/fc980c0530a7e79d16f6894e5e98df9e.jpg"
        // Add more elements as needed
    ]);

    const passImage = (increment) => {
        setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
    };

    const submitForm = async (onClose) => {
        const formURL = `api/user/${session.user.username}/games/${newGame.key}`;

        const requestBody = {
            name: newGame.name,
            tags: tags.join(","),
            timePlayed: timePlayed,
            image: customImage || grids[imageKey],
        };

        try {
            let game = await POST(formURL, session.user.token, requestBody);
            setResultModal("success");
            setGames((prevGames) => [...prevGames, game]);
            onClose();
        } catch (error) {
            setResultModal("error");
        }
    };

    const addTag = () => {
        if(tags.find(tag => tag == tagInput)) return;
        setTags([...tags, tagInput]);
    };

    const removeTag = (tagToRemove) => {
        setTags(tags.filter((tag) => tag !== tagToRemove));
    };

    useEffect(() => {
        console.log(resultModal)
        const timer = setTimeout(() => {
            setResultModal("closed");
        }, 5000);

        return () => clearTimeout(timer);
    }, [resultModal]);

    const renderModalContent = (onClose) => (
        <>
            <ModalHeader className="">Add Game</ModalHeader>
            <ModalBody className="grid grid-cols-2 ">
                <div className="space-y-4">
                    <SearchBar setGame={setNewGame} setGrids={setGrids} />
                    <Image
                        width={600}
                        height={900}
                        alt="Hero Image"
                        src={customImage || grids[imageKey]}
                    />
                    <Input
                        label="Custom Image"
                        placeholder="Enter a URL"
                        type="text"
                        variant="bordered"
                        onChange={(e) => setCustomImage(e.target.value)}
                        required
                    />
                    {!customImage &&
                        <div className="flex justify-between items-center">
                            <Button onClick={() => passImage(-1)} className="h-full w-1/4 text-4xl block z-40">
                                {"<"}
                            </Button>
                            <div className={"w-1/4"}>
                                <Input
                                    value={imageKey + 1}
                                    onChange={(e) => {
                                        setImageKey(e.target.value - 1);
                                    }}
                                    endContent={
                                        <div className={"flex-row flex gap-2"}>
                                            <p>{" / "}</p>
                                            <p>{grids.length}</p>
                                        </div>}
                                >
                                </Input>
                            </div>


                            <Button onClick={() => passImage(+1)} className="h-full w-1/4 text-4xl block z-40">
                                {">"}
                            </Button>
                        </div>
                    }
                    {customImage &&
                        <Button onClick={() => setCustomImage()} className="h-10 w-full text-xl block z-40">
                            Erase Custom Image
                        </Button>
                    }
                </div>

                <div className="flex flex-col gap-4 w-full">
                    <Input
                        label="Time Played"
                        placeholder="Enter time played"
                        type="number"
                        variant="bordered"
                        onChange={(e) => setTimePlayed(e.target.value)}
                        required
                    />

                    <Input
                        label="Tags"
                        placeholder="Enter a tag"
                        type="text"
                        variant="bordered"
                        required
                        value={tagInput}
                        onValueChange={setTagInput}
                        endContent={
                            <Button radius="full" onClick={addTag}>
                                {">"}
                            </Button>
                        }
                    />
                    <div className={"flex flex-row gap-4"}>
                        {tags.map((tag) => (
                            <Chip onClose={() => removeTag(tag)} key={tag}>{tag}</Chip>
                        ))}
                    </div>
                </div>
            </ModalBody>
            <ModalFooter>
                <Button color="danger" variant="flat" onPress={onClose}>
                    Close
                </Button>
                <Button color="primary" onPress={() => submitForm(onClose)}>
                    Add Game
                </Button>
            </ModalFooter>
        </>
    );

    return (
        <>
            <Button onPress={onOpen}>
                <div className="text-4xl font-bold">+</div>
                <div className="text-xl">Add Game</div>
            </Button>
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
            {resultModal === "success" &&
                <Card className="bg-green-500 text-gray-900 text-3xl absolute bottom-3 right-3">
                    <CardBody>
                        <p>Game added correctly.</p>
                    </CardBody>
                </Card>
            }
            {resultModal === "error" &&
                <Card className="bg-red-600 text-gray-900 text-3xl absolute bottom-3 right-3">
                    <CardBody>
                        <p>Error adding game.</p>
                    </CardBody>
                </Card>
            }
        </>
    );
}