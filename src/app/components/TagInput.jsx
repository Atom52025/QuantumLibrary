import {Button, Chip, Input} from "@nextui-org/react";
import {useState} from "react";

export default function TagInput({tags, setTags}){
    // Tag input
    const [tagInput, setTagInput] = useState("");

    const addTag = () => {
        if(tags.find(tag => tag === tagInput)) return;
        setTags([...tags, tagInput]);
    };

    const removeTag = (tagToRemove) => {
        setTags(tags.filter((tag) => tag !== tagToRemove));
    };

    return (
        <>
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
        </>
    );
}