// components/ui/Table.tsx
import React from 'react'

interface TableProps {
  children: React.ReactNode
  striped?: boolean
  bordered?: boolean
  hover?: boolean
  size?: 'sm' | 'lg'
  responsive?: boolean
  variant?: 'dark'
}

interface TableHeaderProps {
  children: React.ReactNode
  variant?: 'dark' | 'light'
}

interface TableBodyProps {
  children: React.ReactNode
}

interface TableRowProps extends React.HTMLAttributes<HTMLTableRowElement> {
  children: React.ReactNode
  variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'
}

interface TableCellProps {
  children: React.ReactNode
  header?: boolean
  scope?: 'col' | 'row'
  className?: string
  colSpan?: number
  style?: React.CSSProperties
}

interface TableHeadProps {
  children: React.ReactNode
  className?: string
  scope?: 'col' | 'row'
}

export function Table({
                        children,
                        striped = false,
                        bordered = false,
                        hover = false,
                        size,
                        responsive = false,
                        variant
                      }: TableProps) {
  const tableClasses = [
    'table',
    striped && 'table-striped',
    bordered && 'table-bordered',
    hover && 'table-hover',
    size && `table-${size}`,
    variant && `table-${variant}`
  ].filter(Boolean).join(' ')

  const table = <table className={tableClasses}>{children}</table>

  return responsive ? (
      <div className="table-responsive">
        {table}
      </div>
  ) : table
}

export function TableHeader({ children, variant }: TableHeaderProps) {
  const classes = variant ? `table-${variant}` : ''
  return <thead className={classes}>{children}</thead>
}

export function TableBody({ children }: TableBodyProps) {
  return <tbody>{children}</tbody>
}

export function TableRow({ children, variant, ...props }: TableRowProps) {
  const classes = variant ? `table-${variant}` : ''
  return <tr className={classes} {...props}>{children}</tr>
}

export function TableCell({ children, header = false, scope, className = '', colSpan, style }: TableCellProps) {
  const Tag = header ? 'th' : 'td'
  return <Tag scope={scope} className={className} colSpan={colSpan} style={style}>{children}</Tag>
}

export function TableHead({ children, className = '', scope = 'col' }: TableHeadProps) {
  return <th className={className} scope={scope}>{children}</th>
}
