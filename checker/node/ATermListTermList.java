/* This file was generated by SableCC (http://www.sablecc.org/). */

package checker.node;

import java.util.*;
import checker.analysis.*;

@SuppressWarnings("nls")
public final class ATermListTermList extends PTermList
{
    private PTerm _term_;
    private final LinkedList<PCommaTerm> _commaTerm_ = new LinkedList<PCommaTerm>();

    public ATermListTermList()
    {
        // Constructor
    }

    public ATermListTermList(
        @SuppressWarnings("hiding") PTerm _term_,
        @SuppressWarnings("hiding") List<PCommaTerm> _commaTerm_)
    {
        // Constructor
        setTerm(_term_);

        setCommaTerm(_commaTerm_);

    }

    @Override
    public Object clone()
    {
        return new ATermListTermList(
            cloneNode(this._term_),
            cloneList(this._commaTerm_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATermListTermList(this);
    }

    public PTerm getTerm()
    {
        return this._term_;
    }

    public void setTerm(PTerm node)
    {
        if(this._term_ != null)
        {
            this._term_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._term_ = node;
    }

    public LinkedList<PCommaTerm> getCommaTerm()
    {
        return this._commaTerm_;
    }

    public void setCommaTerm(List<PCommaTerm> list)
    {
        this._commaTerm_.clear();
        this._commaTerm_.addAll(list);
        for(PCommaTerm e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._term_)
            + toString(this._commaTerm_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._term_ == child)
        {
            this._term_ = null;
            return;
        }

        if(this._commaTerm_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._term_ == oldChild)
        {
            setTerm((PTerm) newChild);
            return;
        }

        for(ListIterator<PCommaTerm> i = this._commaTerm_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PCommaTerm) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}