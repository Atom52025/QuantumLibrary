import Image from "next/image";
import EditGameModal from "@/app/components/modals/EditGameModal";
import {useDisclosure} from "@nextui-org/react";

export default function GameCard({ entry, setGames }) {
    // Modal state
    const {isOpen, onOpen, onOpenChange} = useDisclosure();

    return (
        <>
            <button className=" group z-10 w-full h-full relative"
                    onClick={() => onOpen()}>
                <Image
                    src={entry.image ? entry.image : entry.game.image}
                    alt={entry.game.name}
                    fill
                    sizes={"20vh"}
                    className="rounded-lg"
                />
                <div className="z-20 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100">{entry.game.name}</div>
            </button>
            <EditGameModal userGame={entry} setGames={setGames} isOpen={isOpen} onOpenChange={onOpenChange}/>
        </>
    );
}