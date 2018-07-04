package com.ducnguyen.mysuperchatapplication.Conversation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ducnguyen.mysuperchatapplication.Model.Items.BaseItemAdapter;
import com.ducnguyen.mysuperchatapplication.Model.Items.BaseItemViewHolder;
import com.ducnguyen.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourMessageTextItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.R;
import com.ducnguyen.mysuperchatapplication.Utils.MessageItemListDiffCallback;
import com.ducnguyen.mysuperchatapplication.ViewImageInConversation.ViewImageActivity;
import com.ducnguyen.mysuperchatapplication.databinding.MyAudioMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.MyImgMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.MyLocationMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.MyMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.MyVideoMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.YourAudioMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.YourImgMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.YourLocationMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.YourMessageItemRowBinding;
import com.ducnguyen.mysuperchatapplication.databinding.YourVideoMessageItemRowBinding;

import java.util.ArrayList;

public class MessageRvAdapter extends BaseItemAdapter {

    private static final int MSG_TXT_ME = 0;
    private static final int MSG_TXT_YOU = 1;
    private static final int MSG_IMG_ME = 2;
    private static final int MSG_IMG_YOU = 3;
    private static final int MSG_AUDIO_ME = 4;
    private static final int MSG_AUDIO_YOU = 5;
    private static final int MSG_VIDEO_ME = 6;
    private static final int MSG_VIDEO_YOU = 7;
    private static final int MSG_LOCATION_ME = 8;
    private static final int MSG_LOCATION_YOU = 9;
    ArrayList<BaseMessageItem> messages;
    OnMsgItemClickListener listener;
    Context context;
    MyMessageItemRowBinding myBinding;
    YourMessageItemRowBinding yourbinding;
    MyImgMessageItemRowBinding myImgBinding;
    YourImgMessageItemRowBinding yourImgBinding;
    MyAudioMessageItemRowBinding myAudioBinding;
    YourAudioMessageItemRowBinding yourAudioBinding;
    MyVideoMessageItemRowBinding myVideoBinding;
    YourVideoMessageItemRowBinding yourVideoBinding;
    MyLocationMessageItemRowBinding myLocationBinding;
    YourLocationMessageItemRowBinding yourLocationBinding;

    public MessageRvAdapter(ArrayList<BaseMessageItem> messages, Context context, OnMsgItemClickListener listener) {
        this.messages = messages;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position) instanceof MyMessageTextItem) {
            return MSG_TXT_ME;
        } else if (messages.get(position) instanceof YourMessageTextItem) {
            return MSG_TXT_YOU;
        } else if (messages.get(position) instanceof MyImageMessageItem) {
            return MSG_IMG_ME;
        } else if (messages.get(position) instanceof YourImageMessageItem) {
            return MSG_IMG_YOU;
        } else if (messages.get(position) instanceof MyAudioMessageItem) {
            return MSG_AUDIO_ME;
        } else if (messages.get(position) instanceof YourAudioMessageItem) {
            return MSG_AUDIO_YOU;
        } else if (messages.get(position) instanceof MyVideoMessageItem) {
            return MSG_VIDEO_ME;
        } else if (messages.get(position) instanceof YourVideoMessageItem) {
            return MSG_VIDEO_YOU;
        } else if (messages.get(position) instanceof MyLocationMessageItem) {
            return MSG_LOCATION_ME;
        } else if (messages.get(position) instanceof YourLocationMessageItem) {
            return MSG_LOCATION_YOU;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public BaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MSG_TXT_ME:
                myBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_message_item_row, parent, false);
                return new MyTxtMessageItemViewHolder(myBinding);
            case MSG_TXT_YOU:
                yourbinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_message_item_row, parent, false);
                return new YourTxtMessageItemViewHolder(yourbinding);
            case MSG_IMG_ME:
                myImgBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_img_message_item_row, parent, false);
                return new MyImgMessageItemViewHolder(myImgBinding);
            case MSG_IMG_YOU:
                yourImgBinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_img_message_item_row, parent, false);
                return new YourImgMessageItemViewHolder(yourImgBinding);
            case MSG_AUDIO_ME:
                myAudioBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_audio_message_item_row, parent, false);
                return new MyAudioMessageItemViewHolder(myAudioBinding);
            case MSG_AUDIO_YOU:
                yourAudioBinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_audio_message_item_row, parent, false);
                return new YourAudioMessageItemViewHolder(yourAudioBinding);
            case MSG_VIDEO_ME:
                myVideoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_video_message_item_row, parent, false);
                return new MyVideoMessageItemViewHolder(myVideoBinding);
            case MSG_VIDEO_YOU:
                yourVideoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_video_message_item_row, parent, false);
                return new YourVideoMessageItemViewHolder(yourVideoBinding);
            case MSG_LOCATION_ME:
                myLocationBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_location_message_item_row, parent, false);
                return new MyLocationItemViewHolder(myLocationBinding);
            case MSG_LOCATION_YOU:
                yourLocationBinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_location_message_item_row, parent, false);
                return new YourLocationItemViewHolder(yourLocationBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemViewHolder holder, int position) {
        if (holder instanceof MyTxtMessageItemViewHolder) {
            ((MyTxtMessageItemViewHolder) holder).bind((MyMessageTextItem) messages.get(position));
        } else if (holder instanceof YourTxtMessageItemViewHolder) {
            ((YourTxtMessageItemViewHolder) holder).bind((YourMessageTextItem) messages.get(position));
        } else if (holder instanceof MyImgMessageItemViewHolder) {
            ((MyImgMessageItemViewHolder) holder).bind((MyImageMessageItem) messages.get(position), listener);
        } else if (holder instanceof YourImgMessageItemViewHolder) {
            ((YourImgMessageItemViewHolder) holder).bind((YourImageMessageItem) messages.get(position), listener);
        } else if (holder instanceof MyAudioMessageItemViewHolder) {
            ((MyAudioMessageItemViewHolder) holder).bind((MyAudioMessageItem) messages.get(position), listener);
        } else if (holder instanceof YourAudioMessageItemViewHolder) {
            ((YourAudioMessageItemViewHolder) holder).bind((YourAudioMessageItem) messages.get(position), listener);
        } else if (holder instanceof MyVideoMessageItemViewHolder) {
            ((MyVideoMessageItemViewHolder) holder).bind((MyVideoMessageItem) messages.get(position), listener);
        } else if (holder instanceof YourVideoMessageItemViewHolder) {
            ((YourVideoMessageItemViewHolder) holder).bind((YourVideoMessageItem) messages.get(position), listener);
        } else if (holder instanceof MyLocationItemViewHolder) {
            ((MyLocationItemViewHolder) holder).bind((MyLocationMessageItem) messages.get(position), listener);
        } else if (holder instanceof YourLocationItemViewHolder) {
            ((YourLocationItemViewHolder) holder).bind((YourLocationMessageItem)messages.get(position), listener);
        }
    }


    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    private static class MyTxtMessageItemViewHolder extends BaseItemViewHolder {
        private MyMessageItemRowBinding myBinding;

        public MyTxtMessageItemViewHolder(MyMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyMessageTextItem message) {
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourTxtMessageItemViewHolder extends BaseItemViewHolder {
        private YourMessageItemRowBinding yourBinding;

        public YourTxtMessageItemViewHolder(YourMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourMessageTextItem message) {
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }

    private static class MyImgMessageItemViewHolder extends BaseItemViewHolder {
        private MyImgMessageItemRowBinding myBinding;

        public MyImgMessageItemViewHolder(MyImgMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyImageMessageItem message, OnMsgItemClickListener listener) {
            myBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourImgMessageItemViewHolder extends BaseItemViewHolder {
        private YourImgMessageItemRowBinding yourBinding;

        public YourImgMessageItemViewHolder(YourImgMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourImageMessageItem message, OnMsgItemClickListener listener) {
            yourBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }

    private static class MyAudioMessageItemViewHolder extends BaseItemViewHolder {
        private MyAudioMessageItemRowBinding myBinding;

        public MyAudioMessageItemViewHolder(MyAudioMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyAudioMessageItem message, OnMsgItemClickListener listener) {
            myBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourAudioMessageItemViewHolder extends BaseItemViewHolder {
        private YourAudioMessageItemRowBinding myBinding;

        public YourAudioMessageItemViewHolder(YourAudioMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(YourAudioMessageItem message, OnMsgItemClickListener listener) {
            myBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class MyVideoMessageItemViewHolder extends BaseItemViewHolder {
        private MyVideoMessageItemRowBinding myBinding;

        public MyVideoMessageItemViewHolder(MyVideoMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyVideoMessageItem message, OnMsgItemClickListener listener) {
            myBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourVideoMessageItemViewHolder extends BaseItemViewHolder {
        private YourVideoMessageItemRowBinding yourBinding;

        public YourVideoMessageItemViewHolder(YourVideoMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourVideoMessageItem message, OnMsgItemClickListener listener) {
            yourBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }

    private static class MyLocationItemViewHolder extends BaseItemViewHolder {
        private MyLocationMessageItemRowBinding myBinding;

        public MyLocationItemViewHolder(MyLocationMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyLocationMessageItem message, OnMsgItemClickListener listener) {
            myBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourLocationItemViewHolder extends BaseItemViewHolder {
        private YourLocationMessageItemRowBinding yourBinding;

        public YourLocationItemViewHolder(YourLocationMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourLocationMessageItem message, OnMsgItemClickListener listener) {
            yourBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(message);
                }
            });
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }

    public void updateList(ArrayList<BaseMessageItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MessageItemListDiffCallback(this.messages, newList));
        diffResult.dispatchUpdatesTo(this);
        this.messages = newList;
    }
}
